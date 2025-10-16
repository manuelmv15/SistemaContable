package com.example.demo.clasificacionDocumentos;


import org.springframework.data.jpa.repository.JpaRepository;

public interface clasificacionDocumentoRepository
        extends JpaRepository<clasificacionDocumentoModel, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}