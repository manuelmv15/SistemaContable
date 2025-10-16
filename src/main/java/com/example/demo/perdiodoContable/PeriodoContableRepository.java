package com.example.demo.perdiodoContable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PeriodoContableRepository extends JpaRepository<PeriodoContable, Long> {

    @Query("""
        SELECT p FROM PeriodoContable p
        WHERE (:inicio < p.fechaFin) AND (p.fechaInicio < :fin)
    """)
    List<PeriodoContable> findOverlaps(LocalDate inicio, LocalDate fin);

    @Query("""
        SELECT p FROM PeriodoContable p
        WHERE p.id <> :id AND (:inicio < p.fechaFin) AND (p.fechaInicio < :fin)
    """)
    List<PeriodoContable> findOverlapsExcludingId(Long id, LocalDate inicio, LocalDate fin);
}
