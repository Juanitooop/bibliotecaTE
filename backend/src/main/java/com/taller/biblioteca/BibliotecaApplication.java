package com.taller.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication {
    public static void main(String[] args) {
        // Desde aqui arranca el servidor.
        SpringApplication.run(BibliotecaApplication.class, args);
    }
}
