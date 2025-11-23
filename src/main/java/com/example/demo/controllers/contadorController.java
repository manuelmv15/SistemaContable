package com.example.demo.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaContableService;
import com.example.demo.perdiodoContable.PeriodoContableService;

@Controller
public class contadorController {

    private final CuentaContableService cuentaService;
    private final PeriodoContableService periodosService;

    public contadorController(CuentaContableService cuentaService, PeriodoContableService periodosService) {
        this.cuentaService = cuentaService;
        this.periodosService = periodosService;
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
    public String generarReportes(@RequestParam(required = false) Long periodoId,
                                  @RequestParam(required = false) String desde,
                                  @RequestParam(required = false) String hasta,
                                  Model model){

        //mapeo del los campos 
        model.addAttribute("titulo", "Generar reportes");
        model.addAttribute("periodos", periodosService.listar());
        model.addAttribute("periodoId", periodoId);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        var reportes = cuentaService.buscar(periodoId, desde, hasta);
        model.addAttribute("reportes", reportes);
        
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
