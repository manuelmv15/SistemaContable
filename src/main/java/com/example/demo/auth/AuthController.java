package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> loginForm(
            @RequestParam String usernameOrEmail,
            @RequestParam String password,
            HttpServletRequest request) {

        try {
            var user = authService.login(usernameOrEmail, password);


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


            request.getSession().setAttribute("USER_ID", user.getIdUsuario());
            request.getSession().setAttribute("USERNAME", user.getUsername());


            return ResponseEntity.status(302)
                    .header("Location", "/dashboard")
                    .build();

        } catch (RuntimeException ex) {

            return ResponseEntity.status(302)
                    .header("Location", "/auth/login?error=" + ex.getMessage())
                    .build();
        }
    }
}
