package com.taller.biblioteca.controlador;

import com.taller.biblioteca.dto.UsuarioDTO;
import com.taller.biblioteca.modelo.Usuario;
import com.taller.biblioteca.repositorio.UsuarioRepository;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwords;
    public UsuarioController(UsuarioRepository usuarios, PasswordEncoder passwords) { this.usuarios = usuarios; this.passwords = passwords; }

    @GetMapping
    public Page<Usuario> listar(@RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return usuarios.findByNombreContainingIgnoreCaseOrUsernameContainingIgnoreCase(q, q, Paginas.crear(page, size));
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        Usuario registro = usuarios.findById(id).orElse(null);
        if (registro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }
        return registro;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@Valid @RequestBody UsuarioDTO datos) { return guardar(new Usuario(), datos); }

    @PutMapping("/{id}")
    public Usuario editar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO datos) {
        Usuario usuario = buscar(id);
        return guardar(usuario, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        Usuario usuario = buscar(id);
        usuarios.delete(usuario);
    }

    private Usuario guardar(Usuario usuario, UsuarioDTO datos) {
        String clave = datos.getPassword();
        if (usuario.getId() == null && (clave == null || clave.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clave es obligatoria para un usuario nuevo");
        }
        // Al editar, una clave vacia conserva la clave anterior.
        if (clave != null && !clave.isEmpty()) {
            if (clave.isBlank() || clave.length() < 8 || clave.getBytes(StandardCharsets.UTF_8).length > 72) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clave debe tener al menos 8 caracteres y maximo 72 bytes");
            }
            usuario.setPasswordHash(passwords.encode(clave));
        }
        usuario.setNombre(datos.getNombre().trim());
        usuario.setUsername(datos.getUsername());
        usuario.setEmail(datos.getEmail().trim());
        usuario.setRol(datos.getRol());
        usuario.setEstado(datos.getEstado());
        return usuarios.saveAndFlush(usuario);
    }
}
