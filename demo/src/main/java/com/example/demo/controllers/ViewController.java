package com.example.demo.controllers;

import ch.qos.logback.core.model.Model;
import com.example.demo.usuarios.UsuarioService;
import com.example.demo.usuarios.usuarioModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/auth/login")
    public String mostrarLogin() {
        return "login"; // templates/login.html
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Obtén el usuario actual desde el servicio
        UsuarioService findByUsuario;
        usuarioModel usuario = findByUsuario.findByUsuario(userDetails.getUsername());
        model.addText("usuario", usuario);
        return "dashboard";
    }


}
