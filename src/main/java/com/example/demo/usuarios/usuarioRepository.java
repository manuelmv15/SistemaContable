package com.example.demo.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface usuarioRepository extends JpaRepository<usuarioModel, Long> {
    Optional<usuarioModel> findByUsername(String username); // campo 'username'
    Optional<usuarioModel> findByEmail(String email);
    Optional<usuarioModel> findByUsernameOrEmail(String username, String email);
}
