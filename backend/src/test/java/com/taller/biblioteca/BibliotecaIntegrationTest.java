package com.taller.biblioteca;

import com.fasterxml.jackson.databind.*;
import com.taller.biblioteca.modelo.*;
import com.taller.biblioteca.repositorio.*;
import java.time.*;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Estas pruebas pasan por los controladores sin iniciar sesion.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BibliotecaIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UsuarioRepository usuarios;
    @Autowired AutorRepository autores;
    @Autowired LibroRepository libros;
    @Autowired PrestamoRepository prestamos;
    @Autowired PasswordEncoder passwords;
    Long lectorId;
    Long libroId;
    Long autorId;

    @BeforeEach
    void preparar() throws Exception {
        prestamos.deleteAll(); libros.deleteAll(); autores.deleteAll(); usuarios.deleteAll();
        cuenta("admin", Rol.ADMIN);
        lectorId = cuenta("lector", Rol.LECTOR).getId();
        autorId = respuesta(post("/api/autores"), autor("Gabriel"), 201).get("id").asLong();
        libroId = respuesta(post("/api/libros"), libro("Libro de prueba", "1234567890"), 201).get("id").asLong();
    }

    Usuario cuenta(String username, Rol rol) {
        Usuario u = new Usuario(); u.setNombre(username); u.setUsername(username);
        u.setEmail(username + "@prueba.com"); u.setRol(rol); u.setEstado(Estado.ACTIVO);
        u.setPasswordHash(passwords.encode("Clave12345"));
        return usuarios.save(u);
    }

    Map<String, Object> autor(String nombre) { return Map.of("nombre", nombre, "apellido", "Garcia", "nacionalidad", "Colombiana", "fechaNacimiento", "1927-03-06"); }
    Map<String, Object> libro(String titulo, String isbn) { return Map.of("titulo", titulo, "isbn", isbn, "anio", 2000, "autorId", autorId); }
    Map<String, Object> prestamo(boolean devuelto) { return Map.of("libroId", libroId, "usuarioId", lectorId, "fechaPrestamo", LocalDate.now().toString(), "fechaDevolucion", LocalDate.now().plusDays(7).toString(), "devuelto", devuelto); }

    JsonNode respuesta(MockHttpServletRequestBuilder peticion, Object datos, int codigo) throws Exception {
        if (datos != null) peticion.contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(datos));
        String texto = mvc.perform(peticion).andExpect(status().is(codigo)).andReturn().getResponse().getContentAsString();
        return texto.isEmpty() ? json.createObjectNode() : json.readTree(texto);
    }

    @Test void crudAutoresYLibros() throws Exception {
        respuesta(get("/api/autores/" + autorId), null, 200);
        respuesta(put("/api/autores/" + autorId), autor("Gabriel editado"), 200);
        JsonNode libro = respuesta(put("/api/libros/" + libroId), libro("Titulo editado", "1234567890"), 200);
        assertThat(libro.get("titulo").asText()).isEqualTo("Titulo editado");
        respuesta(get("/api/libros/" + libroId), null, 200);
        respuesta(delete("/api/libros/" + libroId), null, 204);
        respuesta(delete("/api/autores/" + autorId), null, 204);
        respuesta(get("/api/libros/" + libroId), null, 404);
    }

    @Test void crudUsuariosYClaveProtegida() throws Exception {
        Map<String, Object> datos = Map.of("nombre", "Nuevo", "username", "nuevo", "email", "nuevo@prueba.com", "password", "Nueva12345", "rol", "LECTOR", "estado", "ACTIVO");
        JsonNode creado = respuesta(post("/api/usuarios"), datos, 201);
        long id = creado.get("id").asLong();
        assertThat(creado.has("passwordHash")).isFalse();
        assertThat(usuarios.findById(id).orElseThrow().getPasswordHash()).startsWith("$2");
        respuesta(get("/api/usuarios/" + id), null, 200);
        respuesta(put("/api/usuarios/" + id), Map.of("nombre", "Editado", "username", "nuevo", "email", "nuevo@prueba.com", "password", "", "rol", "LECTOR", "estado", "ACTIVO"), 200);
        assertThat(passwords.matches("Nueva12345", usuarios.findById(id).orElseThrow().getPasswordHash())).isTrue();
        respuesta(delete("/api/usuarios/" + id), null, 204);
    }

    @Test void crudPrestamos() throws Exception {
        JsonNode creado = respuesta(post("/api/prestamos"), prestamo(false), 201);
        long id = creado.get("id").asLong();
        assertThat(creado.get("usuario").has("passwordHash")).isFalse();
        respuesta(get("/api/prestamos/" + id), null, 200);
        JsonNode editado = respuesta(put("/api/prestamos/" + id), prestamo(true), 200);
        assertThat(editado.get("devuelto").asBoolean()).isTrue();
        respuesta(delete("/api/prestamos/" + id), null, 204);
        assertThat(prestamos.count()).isZero();
    }



    @Test void libroNoSePrestaDosVecesYSeLiberaAlDevolver() throws Exception {
        long id = respuesta(post("/api/prestamos"), prestamo(false), 201).get("id").asLong();
        respuesta(post("/api/prestamos"), prestamo(false), 409);
        respuesta(put("/api/prestamos/" + id), prestamo(true), 200);
        respuesta(post("/api/prestamos"), prestamo(false), 201);
    }

    @Test void sePuedeDevolverUnPrestamoDeUsuarioInactivo() throws Exception {
        long id = respuesta(post("/api/prestamos"), prestamo(false), 201).get("id").asLong();
        Usuario lector = usuarios.findById(lectorId).orElseThrow();
        lector.setEstado(Estado.INACTIVO); usuarios.save(lector);
        respuesta(put("/api/prestamos/" + id), prestamo(true), 200);
        respuesta(post("/api/prestamos"), prestamo(false), 400);
    }

    @Test void validacionesYRelaciones() throws Exception {
        respuesta(post("/api/autores"), autor(""), 400);
        respuesta(post("/api/libros"), libro("Repetido", "1234567890"), 409);
        respuesta(delete("/api/autores/" + autorId), null, 409);
        respuesta(post("/api/prestamos"), Map.of("libroId", libroId, "usuarioId", lectorId, "fechaPrestamo", LocalDate.now().toString(), "fechaDevolucion", "2000-01-01", "devuelto", false), 400);

        respuesta(get("/api/libros?page=-1"), null, 400);
        respuesta(get("/api/libros?size=101"), null, 400);
    }

    @Test void busquedaYPaginacion() throws Exception {
        respuesta(post("/api/libros"), libro("Segundo libro", "0987654321"), 201);
        JsonNode pagina = respuesta(get("/api/libros?size=1&page=1&q=Gabriel"), null, 200);
        assertThat(pagina.get("content").size()).isEqualTo(1);
        assertThat(pagina.get("totalPages").asInt()).isEqualTo(2);
        assertThat(respuesta(get("/api/libros?q=Segundo"), null, 200).get("totalElements").asInt()).isEqualTo(1);
        respuesta(post("/api/prestamos"), prestamo(false), 201);
        assertThat(respuesta(get("/api/prestamos?q=lector"), null, 200).get("totalElements").asInt()).isEqualTo(1);
    }


}
