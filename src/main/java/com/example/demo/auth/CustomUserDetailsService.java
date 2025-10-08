package com.example.demo.auth;

import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final usuarioRepository repo;

    public CustomUserDetailsService(usuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        usuarioModel u = repo.findByUsername(usernameOrEmail)
                .or(() -> repo.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

        if (!Boolean.TRUE.equals(u.getEstado())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        String role = (u.getRol() != null && u.getRol().getNombre() != null)
                ? u.getRol().getNombre().toUpperCase().replace(" ", "_")
                : "USER";

        return User.withUsername(u.getUsername())       // <-- ahora sí existe
                .password(u.getContrasenia())           // hash BCrypt
                .authorities(new SimpleGrantedAuthority("ROLE_" + role))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!Boolean.TRUE.equals(u.getEstado()))
                .build();
    }
}
