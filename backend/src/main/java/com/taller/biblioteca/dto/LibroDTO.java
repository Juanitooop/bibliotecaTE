package com.taller.biblioteca.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.taller.biblioteca.modelo.*;

// Aqui recibimos y revisamos los datos del formulario.
public class LibroDTO {
    @NotBlank @Size(max = 150)
    private String titulo;

    @NotBlank @Pattern(regexp = "[0-9Xx-]{10,20}", message = "El ISBN debe tener entre 10 y 20 caracteres: numeros, X o guiones")
    private String isbn;

    @NotNull @Min(1) @Max(2100)
    private Integer anio;

    @NotNull @Positive
    private Long autorId;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

}
