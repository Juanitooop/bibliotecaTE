package com.taller.biblioteca.repositorio;

import com.taller.biblioteca.modelo.Autor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring crea las consultas a partir del nombre del metodo.
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Page<Autor> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido, Pageable pagina);
}
