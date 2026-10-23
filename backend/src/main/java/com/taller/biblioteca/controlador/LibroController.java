package com.taller.biblioteca.controlador;

import com.taller.biblioteca.dto.LibroDTO;
import com.taller.biblioteca.modelo.Libro;
import com.taller.biblioteca.repositorio.LibroRepository;
import com.taller.biblioteca.repositorio.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroRepository libros;
    private final AutorRepository autores;
    public LibroController(LibroRepository libros, AutorRepository autores) { this.libros = libros; this.autores = autores; }

    @GetMapping
    public Page<Libro> listar(@RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return libros.buscar(q, Paginas.crear(page, size));
    }

    @GetMapping("/{id}")
    public Libro buscar(@PathVariable Long id) {
        Libro registro = libros.findById(id).orElse(null);
        if (registro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro no existe");
        }
        return registro;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public Libro crear(@Valid @RequestBody LibroDTO datos) { return guardar(new Libro(), datos); }

    @PutMapping("/{id}")

    public Libro editar(@PathVariable Long id, @Valid @RequestBody LibroDTO datos) { return guardar(buscar(id), datos); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void eliminar(@PathVariable Long id) { libros.delete(buscar(id)); }

    private Libro guardar(Libro libro, LibroDTO datos) {
        libro.setTitulo(datos.getTitulo().trim());
        libro.setIsbn(datos.getIsbn().trim());
        libro.setAnio(datos.getAnio());
        libro.setAutor(autores.findById(datos.getAutorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El autor no existe")));
        return libros.saveAndFlush(libro);
    }
}
