package com.example.demo.documentoFuente;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

public interface documentoFuenteRepository extends JpaRepository<documentoFuenteModel, Long> {

    @EntityGraph(attributePaths = "clasificacion")
    List<documentoFuenteModel> findAll();

    @EntityGraph(attributePaths = "clasificacion")
    List<documentoFuenteModel> findByClasificacion_IdClasificacion(Long idClasificacion);
}