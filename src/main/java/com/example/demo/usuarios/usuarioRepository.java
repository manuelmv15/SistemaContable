package com.example.demo.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface usuarioRepository extends JpaRepository<usuarioModel, Long> {
    // Usa nombres de PROPIEDAD Java (no el nombre de columna).
    Optional<usuarioModel> findByUsername(String username);
    Optional<usuarioModel> findByEmail(String email);

}
