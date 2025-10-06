// src/main/java/com/example/demo/usuarios/UsuarioService.java
package com.example.demo.usuarios;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class usuarioService {
    private final usuarioRepository repo;

    public usuarioService(usuarioRepository repo) {
        this.repo = repo;
    }

    public usuarioModel findByUsuario(String usuario) {
        // buscar por username; si no existe, lanza 404 lógico
        return repo.findByUsername(usuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
