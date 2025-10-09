package com.example.demo.config;

import com.example.demo.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.HiddenHttpMethodFilter;

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
                .authenticationProvider(authProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error/**", "/auth/**", "/public/**", "/", "/index.html",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/superadmin/**").hasRole("SUPERADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")   // <- aquí SÍ se exige CSRF
                        .requestMatchers("/contador/**").hasRole("CONTADOR")
                        .requestMatchers("/auditor/**").hasRole("AUDITOR")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler(denied))
                .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable());

        return http.build();
    }

    @Configuration
    public class WebConfig {
        @Bean
        HiddenHttpMethodFilter hiddenHttpMethodFilter() {
            return new HiddenHttpMethodFilter();
        }
    }

}
