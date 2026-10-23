package com.taller.biblioteca.controlador;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Paginas {
    public static Pageable crear(int pagina, int tamano) {
        if (pagina < 0 || tamano < 1 || tamano > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pagina minima: 0. Tamano permitido: 1 a 100");
        }
        return PageRequest.of(pagina, tamano, Sort.by("id").ascending());
    }
}
