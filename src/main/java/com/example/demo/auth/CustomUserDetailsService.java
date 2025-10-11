package com.example.demo.auth;

import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final usuarioRepository repo;

    public CustomUserDetailsService(usuarioRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        usuarioModel u = repo
                .findByUsername(usernameOrEmail)
                .or(() -> repo.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario/email: " + usernameOrEmail));
        
        if (!Boolean.TRUE.equals(u.getEstado())) {
            throw new DisabledException("Usuario deshabilitado");
        }

        String role = (u.getRol() != null && u.getRol().getNombre() != null)
                ? "ROLE_" + u.getRol().getNombre().toUpperCase().replace(" ", "_")
                : "ROLE_USER";

        return User.withUsername(u.getUsername())
                .password(u.getContrasenia())
                .authorities(new SimpleGrantedAuthority(role))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!Boolean.TRUE.equals(u.getEstado()))
                .build();
    }
}

