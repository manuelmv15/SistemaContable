package com.example.demo.usuarios;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

@Service
public class usuarioService {

    private final usuarioRepository repo;

    public usuarioService(usuarioRepository repo) {
        this.repo = repo;
    }

    // Buscar un usuario específico por nombre de usuario
    public usuarioModel findByUsuario(String usuario) {
        return repo.findByUsername(usuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    // ✅ Nuevo método para obtener todos los usuarios
    public List<usuarioModel> findAll() {
        return repo.findAll();
    }
}
