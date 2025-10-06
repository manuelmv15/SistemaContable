package com.example.demo.auth;

import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final usuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(usuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public usuarioModel login(String usernameOrEmail, String rawPassword) {
        var user = usuarioRepo.findByUsername(usernameOrEmail)
                .or(() -> usuarioRepo.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!Boolean.TRUE.equals(user.getEstado())) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(rawPassword, user.getContrasenia())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return user;
    }
}