package com.example.demo.config;

import com.example.demo.auth.CustomUserDetailsService;
import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService uds;

    @Value("${app.user.default-password}")
    private String defaultPassword;

    public SecurityConfig(CustomUserDetailsService uds) {
        this.uds = uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {
        var p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(PasswordEncoder encoder,
                                                                     @Lazy usuarioService usuarioService) {
        return (request, response, authentication) -> {
            usuarioModel u = usuarioService.findByUsername(authentication.getName()).orElse(null); //falla en orElse
            if (u != null && encoder.matches(defaultPassword, u.getContrasenia())) {
                response.sendRedirect("/cambiar-password");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider authProvider,
                                           AuthenticationSuccessHandler successHandler) throws Exception {
        var denied = new AccessDeniedHandlerImpl();
        denied.setErrorPage("/error/403");

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                "/error/**", "/auth/**", "/public/**",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico", "/"
                        )
                )
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error/**", "/auth/**", "/public/**", "/", "/index.html",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/superadmin/**").hasRole("SUPERADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/contador/**").hasRole("CONTADOR")
                        .requestMatchers("/auditor/**").hasRole("AUDITOR")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler(denied))
                .logout(l -> l.logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .httpBasic(h -> h.disable())
                .formLogin(f -> f
                        .loginPage("/auth/login").permitAll()
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("usernameOrEmail")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureHandler((request, response, ex) -> {
                            String msg = "Error de autenticación";
                            Throwable t = ex;
                            while (t != null) {
                                if (t instanceof DisabledException) { msg = "Usuario deshabilitado"; break; }
                                if (t instanceof BadCredentialsException) { msg = "Usuario o contraseña inválidos"; break; }
                                t = t.getCause();
                            }
                            String encoded = URLEncoder.encode(msg, StandardCharsets.UTF_8);
                            response.sendRedirect("/auth/login?error=" + encoded);
                        })
                );

        return http.build();
    }
}
