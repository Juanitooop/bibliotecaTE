package com.taller.biblioteca.repositorio;

import com.taller.biblioteca.modelo.Prestamo;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    boolean existsByLibroIdAndDevueltoFalseAndIdNot(Long libroId, Long id);

    // Buscamos por libro, autor o usuario. En este avance se muestran todos los prestamos.
    @Query("select p from Prestamo p where lower(p.libro.titulo) like lower(concat('%', :q, '%')) or lower(concat(p.libro.autor.nombre, ' ', p.libro.autor.apellido)) like lower(concat('%', :q, '%')) or lower(p.usuario.nombre) like lower(concat('%', :q, '%')) or lower(p.usuario.username) like lower(concat('%', :q, '%'))")
    Page<Prestamo> buscar(@Param("q") String q, Pageable pagina);
}
