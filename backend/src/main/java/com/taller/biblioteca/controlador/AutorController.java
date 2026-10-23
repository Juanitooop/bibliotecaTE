package com.taller.biblioteca.controlador;

import com.taller.biblioteca.dto.AutorDTO;
import com.taller.biblioteca.modelo.Autor;
import com.taller.biblioteca.repositorio.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    private final AutorRepository autores;
    public AutorController(AutorRepository autores) { this.autores = autores; }

    @GetMapping
    public Page<Autor> listar(@RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return autores.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(q, q, Paginas.crear(page, size));
    }

    @GetMapping("/{id}")
    public Autor buscar(@PathVariable Long id) {
        Autor registro = autores.findById(id).orElse(null);
        if (registro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El autor no existe");
        }
        return registro;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public Autor crear(@Valid @RequestBody AutorDTO datos) {
        return guardar(new Autor(), datos);
    }

    @PutMapping("/{id}")

    public Autor editar(@PathVariable Long id, @Valid @RequestBody AutorDTO datos) {
        return guardar(buscar(id), datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void eliminar(@PathVariable Long id) { autores.delete(buscar(id)); }

    private Autor guardar(Autor autor, AutorDTO datos) {
        // Pasamos los datos del formulario al autor.
        autor.setNombre(datos.getNombre().trim());
        autor.setApellido(datos.getApellido().trim());
        autor.setNacionalidad(datos.getNacionalidad().trim());
        autor.setFechaNacimiento(datos.getFechaNacimiento());
        return autores.saveAndFlush(autor);
    }
}
