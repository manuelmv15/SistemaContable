package com.example.demo.config;

import com.example.demo.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

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
        // handler para 403 -> forward a /error/403
        var denied = new org.springframework.security.web.access.AccessDeniedHandlerImpl();
        denied.setErrorPage("/error/403");

        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authProvider())
                .authorizeHttpRequests(auth -> auth
                        // públicas (incluye errores)
                        .requestMatchers("/error/**", "/auth/**", "/public/**", "/", "/index.html",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // por rol
                        .requestMatchers("/superadmin/**").hasRole("SUPERADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/contador/**").hasRole("CONTADOR")
                        .requestMatchers("/auditor/**").hasRole("AUDITOR")

                        // resto
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler(denied))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable());

        return http.build();
    }


}
