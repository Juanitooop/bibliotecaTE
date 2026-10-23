package com.taller.biblioteca.config;

import com.taller.biblioteca.modelo.*;
import com.taller.biblioteca.repositorio.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(name = "app.datos-ejemplo", havingValue = "true")
public class DatosEjemplo {
    @Bean
    CommandLineRunner cuentas(UsuarioRepository usuarios, PasswordEncoder passwords) {
        return args -> {
            // Solo creamos las cuentas cuando la tabla esta vacia.
            if (usuarios.count() == 0) {
                crear(usuarios, passwords, "Administrador", "admin", "Admin12345", Rol.ADMIN);
                crear(usuarios, passwords, "Lector de prueba", "lector", "Lector12345", Rol.LECTOR);
            }
        };
    }

    private void crear(UsuarioRepository usuarios, PasswordEncoder passwords, String nombre, String username, String clave, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsername(username);
        usuario.setEmail(username + "@biblioteca.local");
        usuario.setPasswordHash(passwords.encode(clave));
        usuario.setRol(rol);
        usuario.setEstado(Estado.ACTIVO);
        usuarios.save(usuario);
    }
}
