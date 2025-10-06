package com.example.demo.rol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface rolRepository extends JpaRepository<rolModel, Long> {
    Optional<rolModel> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
