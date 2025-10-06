package com.example.demo.config;

import com.example.demo.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService uds;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService uds, PasswordEncoder passwordEncoder) {
        this.uds = uds;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        var p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(passwordEncoder);
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desactiva CSRF (para APIs REST)
                .authenticationProvider(authProvider())
                .authorizeHttpRequests(auth -> auth
                        // 🔓 rutas públicas
                        .requestMatchers("/auth/**", "/public/**", "/", "/index.html").permitAll()
                        // 🔒 todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                // deshabilita login básico (evita el popup)
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable());

        return http.build();
    }
}
