package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class auditorController {
    
    @GetMapping("/auditor/dashboard")
    public String adminDashboard() {
        return "auditor/dashboard";
    }

    @GetMapping("/auditor/generar-reportes")
    public String generarReportes(){
        return "auditor/generarReportes";
    }

    @GetMapping("/auditor/libro-diario")
    public String libroDiario(){
        return "auditor/libroDiario";
    }

    @GetMapping("/auditor/libro-mayor")
    public String libroMayor(){
        return "auditor/libroMayor";
    }

    @GetMapping("/auditor/auditoria")
    public String auditoria(){
        return "auditor/auditoria";
    }
}
