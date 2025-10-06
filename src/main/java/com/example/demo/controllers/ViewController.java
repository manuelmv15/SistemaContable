// src/main/java/com/example/demo/controllers/ViewController.java
package com.example.demo.controllers;

import com.example.demo.usuarios.usuarioService;
import com.example.demo.usuarios.usuarioModel;

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
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        usuarioModel usuario = usuarioService.findByUsuario(userDetails.getUsername());

        model.addAttribute("usuario", usuario);
        // si mapeaste la relación @ManyToOne rol:
        // model.addAttribute("rol", usuario.getRol().getNombre());

        return "dashboard";
    }
}
