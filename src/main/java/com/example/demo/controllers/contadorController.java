package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class contadorController {

    @GetMapping("/contador/dashboard")
    public String dashboard() {
        return "contador/dashboard";
    }

    @GetMapping("/contador/registrarPartida")
    public String registrarPartida(){
        return "contador/registrarPartida";
    }
}
