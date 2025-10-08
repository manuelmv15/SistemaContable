// src/main/java/com/example/demo/controllers/ViewController.java
package com.example.demo.controllers;

import com.example.demo.usuarios.usuarioService;
import com.example.demo.usuarios.usuarioModel;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// ✅ IMPORT CORRECTO:
import org.springframework.ui.Model;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class ViewController {


    @GetMapping("/auth/login")
    public String mostrarLogin() {
        return "login"; // templates/login.html
    }

    private final usuarioService usuarioService;

    public ViewController(usuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth == null) return "redirect:/auth/login";

        String rol = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse("");

        return switch (rol) {
            case "ROLE_SUPERADMIN" -> "redirect:/superadmin/dashboard";
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
            case "ROLE_CONTADOR" -> "redirect:/contador/dashboard";
            case "ROLE_AUDITOR" -> "redirect:/auditor/dashboard";
            default -> "redirect:/auth/login";
        };
    }

}
