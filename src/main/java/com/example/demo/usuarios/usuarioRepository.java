package com.example.demo.usuarios;

import com.example.demo.usuarios.usuarioModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

@Repository
public interface usuarioRepository extends JpaRepository<usuarioModel, Long> {

    @EntityGraph(attributePaths = "rol")
    Optional<usuarioModel> findByUsername(String username);

    @EntityGraph(attributePaths = "rol")
    Optional<usuarioModel> findByEmail(String email);

    @EntityGraph(attributePaths = "rol")
    Optional<usuarioModel> findByUsernameOrEmail(String username, String email);
}
