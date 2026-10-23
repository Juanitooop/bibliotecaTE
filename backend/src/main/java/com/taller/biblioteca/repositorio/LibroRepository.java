package com.taller.biblioteca.repositorio;

import com.taller.biblioteca.modelo.Libro;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("select l from Libro l where lower(l.titulo) like lower(concat('%', :q, '%')) or lower(concat(l.autor.nombre, ' ', l.autor.apellido)) like lower(concat('%', :q, '%'))")
    Page<Libro> buscar(@Param("q") String q, Pageable pagina);

}
