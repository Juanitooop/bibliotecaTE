package com.taller.biblioteca.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.taller.biblioteca.modelo.*;

// Aqui recibimos y revisamos los datos del formulario.
public class PrestamoDTO {
    @NotNull @Positive
    private Long libroId;

    @NotNull @Positive
    private Long usuarioId;

    @NotNull @PastOrPresent
    private LocalDate fechaPrestamo;

    @NotNull
    private LocalDate fechaDevolucion;

    @NotNull
    private Boolean devuelto;

    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public Boolean getDevuelto() { return devuelto; }
    public void setDevuelto(Boolean devuelto) { this.devuelto = devuelto; }

}
