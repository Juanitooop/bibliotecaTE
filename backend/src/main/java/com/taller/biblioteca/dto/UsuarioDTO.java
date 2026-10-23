package com.taller.biblioteca.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.taller.biblioteca.modelo.*;

// Aqui recibimos y revisamos los datos del formulario.
public class UsuarioDTO {
    @NotBlank @Size(max = 100)
    private String nombre;

    @NotBlank @Pattern(regexp = "[a-zA-Z0-9_.-]{3,50}", message = "El usuario debe tener de 3 a 50 letras, numeros, puntos, guiones o guion bajo")
    private String username;

    @NotBlank @Email @Size(max = 120)
    private String email;

    @Size(max = 72)
    private String password;

    @NotNull
    private Rol rol;

    @NotNull
    private Estado estado;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

}
