package com.taller.biblioteca.controlador;

import java.util.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErroresController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> negocio(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("mensaje", Objects.requireNonNullElse(error.getReason(), "No se pudo realizar la operacion")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validacion(MethodArgumentNotValidException error) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (var campo : error.getBindingResult().getFieldErrors()) {
            campos.put(campo.getField(), campo.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of("mensaje", "Revisa los datos del formulario", "campos", campos));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> datosRepetidos() {
        // La base de datos tambien revisa los datos repetidos y las relaciones.
        return ResponseEntity.status(409).body(Map.of("mensaje", "Hay un ISBN, usuario o correo repetido, o el registro tiene datos relacionados y no se puede eliminar."));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> formato() {
        return ResponseEntity.badRequest().body(Map.of("mensaje", "Hay un dato con formato incorrecto. Revisa fechas, numeros, rol y estado."));
    }
}
