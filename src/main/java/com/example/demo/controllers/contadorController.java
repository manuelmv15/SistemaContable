package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class contadorController {

    //Referencia de ruta a la vista /contador/dashboard.html
    @GetMapping("/contador/dashboard")
    public String dashboard() {
        return "contador/dashboard";
    }

    //Referencia de ruta a la vista /contador/registrarPartida.html
    @GetMapping("/contador/registrar-partida")
    public String registrarPartida(){
        return "contador/registrarPartida";
    }

    //Referencia de ruta a la vista /contador/subirDocumento.html
    @GetMapping("/contador/subir-doc")
    public String subirDocs(){
        return "contador/subirDocs";
    }

    //Referencia de ruta a la vista /contador/generarReportes.html
    @GetMapping("/contador/generar-reportes")
    public String generarReportes(){
        return "contador/generarReportes";
    }

    //Referencia de ruta a la vista /contador/libroDiario.html
    @GetMapping("/contador/libro-diario")
    public String libroDiario(){
        return "contador/libroDiario";
    }    

    //Referencia de ruta a la vista /contador/libroMayor.html
    @GetMapping("/contador/libro-mayor")
    public String libroMayor(){
        return "contador/libroMayor";
    }    
}
