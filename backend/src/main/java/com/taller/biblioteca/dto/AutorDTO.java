package com.taller.biblioteca.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.taller.biblioteca.modelo.*;

// Aqui recibimos y revisamos los datos del formulario.
public class AutorDTO {
    @NotBlank @Size(max = 80)
    private String nombre;

    @NotBlank @Size(max = 80)
    private String apellido;

    @NotBlank @Size(max = 80)
    private String nacionalidad;

    @NotNull @PastOrPresent
    private LocalDate fechaNacimiento;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

}
