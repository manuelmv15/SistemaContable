package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Procesa el login del formulario
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> loginForm(
            @RequestParam String usernameOrEmail,
            @RequestParam String password,
            HttpServletRequest request) {

        try {
            var user = authService.login(usernameOrEmail, password);

            // 🔒 Crear autenticación en el contexto de Spring Security
            var rol = (user.getRol() != null && user.getRol().getNombre() != null)
                    ? user.getRol().getNombre().toUpperCase().replace(" ", "_")
                    : "USER";

            var authorities = java.util.List.of(
                    new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + rol)
            );

            var principal = org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getContrasenia())
                    .authorities(authorities)
                    .build();

            var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    principal, null, authorities);

            var context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            org.springframework.security.core.context.SecurityContextHolder.setContext(context);

            request.getSession(true).setAttribute(
                    org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );

            // ✅ Guardar datos del usuario en sesión
            request.getSession().setAttribute("USER_ID", user.getIdUsuario());
            request.getSession().setAttribute("USERNAME", user.getUsername());

            // Redirigir al dashboard
            return ResponseEntity.status(302)
                    .header("Location", "/dashboard")
                    .build();

        } catch (RuntimeException ex) {
            // ❌ Si falla, redirige al login con mensaje de error
            return ResponseEntity.status(302)
                    .header("Location", "/auth/login?error=" + ex.getMessage())
                    .build();
        }
    }
}
