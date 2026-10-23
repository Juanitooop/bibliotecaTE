package com.taller.biblioteca.controlador;

import com.taller.biblioteca.dto.PrestamoDTO;
import com.taller.biblioteca.modelo.*;
import com.taller.biblioteca.repositorio.PrestamoRepository;
import com.taller.biblioteca.servicio.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    private final PrestamoRepository prestamos;
    private final PrestamoService servicio;
    public PrestamoController(PrestamoRepository prestamos, PrestamoService servicio) { this.prestamos = prestamos; this.servicio = servicio; }

    @GetMapping
    public Page<Prestamo> listar(@RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return prestamos.buscar(q, Paginas.crear(page, size));
    }

    @GetMapping("/{id}")
    public Prestamo buscar(@PathVariable Long id) { return servicio.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public Prestamo crear(@Valid @RequestBody PrestamoDTO datos) { return servicio.guardar(null, datos); }

    @PutMapping("/{id}")

    public Prestamo editar(@PathVariable Long id, @Valid @RequestBody PrestamoDTO datos) { return servicio.guardar(id, datos); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void eliminar(@PathVariable Long id) { prestamos.delete(servicio.buscar(id)); }
}
