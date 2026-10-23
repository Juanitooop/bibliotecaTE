package com.taller.biblioteca.servicio;

import com.taller.biblioteca.dto.PrestamoDTO;
import com.taller.biblioteca.modelo.*;
import com.taller.biblioteca.repositorio.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PrestamoService {
    private final PrestamoRepository prestamos;
    private final LibroRepository libros;
    private final UsuarioRepository usuarios;

    public PrestamoService(PrestamoRepository prestamos, LibroRepository libros, UsuarioRepository usuarios) {
        this.prestamos = prestamos; this.libros = libros; this.usuarios = usuarios;
    }

    public Prestamo buscar(Long id) {
        Prestamo prestamo = prestamos.findById(id).orElse(null);
        if (prestamo == null) {
            throw error(HttpStatus.NOT_FOUND, "El prestamo no existe");
        }
        return prestamo;
    }

    // Si falla el guardado, se cancela la operacion en la base de datos.
    @Transactional
    public Prestamo guardar(Long id, PrestamoDTO datos) {
        // Si hay id, buscamos el prestamo que vamos a editar.
        Prestamo prestamo = new Prestamo();
        if (id != null) {
            prestamo = buscar(id);
        }
        if (datos.getFechaDevolucion().isBefore(datos.getFechaPrestamo())) {
            throw error(HttpStatus.BAD_REQUEST, "La devolucion no puede ser anterior al prestamo");
        }
        Usuario lector = usuarios.findById(datos.getUsuarioId()).orElse(null);
        if (lector == null) {
            throw error(HttpStatus.NOT_FOUND, "El lector no existe");
        }
        // Podemos devolver un prestamo antiguo aunque el lector ahora este inactivo.
        boolean mismoUsuario = id != null && prestamo.getUsuario().getId().equals(lector.getId());
        if (!mismoUsuario && (lector.getRol() != Rol.LECTOR || lector.getEstado() != Estado.ACTIVO)) {
            throw error(HttpStatus.BAD_REQUEST, "El usuario debe ser un lector activo");
        }
        Libro libro = libros.findById(datos.getLibroId()).orElse(null);
        if (libro == null) {
            throw error(HttpStatus.NOT_FOUND, "El libro no existe");
        }
        Long idActual = -1L;
        if (id != null) {
            idActual = id;
        }
        // Revisamos si el libro ya tiene un prestamo pendiente.
        if (!datos.getDevuelto() && prestamos.existsByLibroIdAndDevueltoFalseAndIdNot(libro.getId(), idActual)) {
            throw error(HttpStatus.CONFLICT, "Este libro ya tiene un prestamo pendiente");
        }
        prestamo.setLibro(libro);
        prestamo.setUsuario(lector);
        prestamo.setFechaPrestamo(datos.getFechaPrestamo());
        prestamo.setFechaDevolucion(datos.getFechaDevolucion());
        prestamo.setDevuelto(datos.getDevuelto());
        return prestamos.saveAndFlush(prestamo);
    }

    private ResponseStatusException error(HttpStatus estado, String mensaje) { return new ResponseStatusException(estado, mensaje); }
}
