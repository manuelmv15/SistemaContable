package com.example.demo.controllers;

import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final usuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.user.default-password}")
    private String defaultPassword;

    @GetMapping("/cambiar-password")
    public String formCambiar(){ return "auth/cambiar-password"; }

    @PostMapping("/cambiar-password")
    public String cambiar(@RequestParam String nueva,
                          @RequestParam String repetir,
                          Principal principal,
                          RedirectAttributes ra){
        if (!nueva.equals(repetir)) {
            ra.addFlashAttribute("err", "Las contraseñas no coinciden.");
            return "redirect:/cambiar-password";
        }
        if (nueva.equals(defaultPassword)) {
            ra.addFlashAttribute("err", "No puedes usar la contraseña por defecto.");
            return "redirect:/cambiar-password";
        }
        usuarioModel u = usuarioService.findByUsername(principal.getName()).orElseThrow();
        usuarioService.cambiarPassword(u.getIdUsuario(), nueva);
        ra.addFlashAttribute("ok", "Contraseña actualizada.");
        return "redirect:/";
    }
}
