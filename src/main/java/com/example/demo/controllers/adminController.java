package com.example.demo.controllers;

import com.example.demo.usuarios.usuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final usuarioService usuarioService;

    // inyección por constructor
    public adminController(usuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/Clasificacion-Documentos")
    public String clasificacionDocumentos() {
        return "admin/Clasificacion-Documentos";
    }

    @GetMapping("/Periodos-Contables")
    public String periodosContables() {
        return "admin/Periodos-Contables";
    }

    @GetMapping("/Consultar-Auditorias")
    public String consultarAuditorias() {
        return "admin/Consultar-Auditorias";
    }

    @GetMapping("/Gestionar-Usuarios")
    public String gestionarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "admin/Gestionar-Usuarios";
    }


}





