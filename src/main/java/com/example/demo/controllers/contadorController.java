package com.example.demo.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaContableService;

@Controller
public class contadorController {

    private final CuentaContableService cuentaService;

    public contadorController(CuentaContableService cuentaService) {
        this.cuentaService = cuentaService;
    }

    //Referencia de ruta a la vista /contador/dashboard.html
    @GetMapping("/contador/dashboard")
    public String dashboard() {
        return "contador/dashboard";
    }

    //Referencia de ruta a la vista /contador/registrarPartida.html
    @GetMapping("/contador/registrar-partida")
    public String registrarPartida(Model model){

        List<CuentaContable> cuentas = cuentaService.listar(); //obtener cuentas

        model.addAttribute("todasLasCuentas", cuentas); //nombre para llamar desde el front

        return "contador/registrarPartida";
    }

    //Referencia de ruta a la vista /contador/subirDocumento.html
    @GetMapping("/contador/subir-doc")
    public String subirDocs(){
        return "contador/subirDocumento";
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
    public String libroMayor(Model model){
        
        List<CuentaContable> cuentas = cuentaService.listar(); //obtener cuentas

        model.addAttribute("todasLasCuentas", cuentas); //nombre para llamar desde el front

        return "contador/libroMayor";
    }    
}
