package com.example.demo.auth;

import com.example.demo.usuarios.usuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final usuarioRepository repo;
    public CustomUserDetailsService(usuarioRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repo.findByUsername(username)
                .or(() -> repo.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

        if (!Boolean.TRUE.equals(u.getEstado())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        // Spring antepone "ROLE_" automáticamente a lo que pongas en roles()
        String rol = (u.getRol() != null && u.getRol().getNombre() != null)
                ? u.getRol().getNombre().toUpperCase().replace(" ", "_")
                : "USER";

        return User.withUsername(u.getUsername())
                .password(u.getContrasenia()) // hash BCrypt de tu BD
                .roles(rol)
                .build();
    }
}
