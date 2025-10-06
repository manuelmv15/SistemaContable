package com.example.demo.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface usuarioRepository extends JpaRepository<usuarioModel, Long> {
    Optional<usuarioModel> findByEmail(String email);
    Optional<usuarioModel> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
