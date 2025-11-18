package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaContableService;
import com.example.demo.tipocuenta.TipoCuenta;
import com.example.demo.tipocuenta.TipoCuentaService;

@Controller
@RequestMapping("/admin")
public class AdminCatalogoCuentasController {

    private final TipoCuentaService tipoSrv;
    private final CuentaContableService cuentaSrv;

    //Tipo de cuenta y cuentas
    public AdminCatalogoCuentasController(TipoCuentaService tipoSrv, CuentaContableService cuentaSrv) {
        this.tipoSrv = tipoSrv;
        this.cuentaSrv = cuentaSrv;
    }

    //
    @GetMapping("/Gestionar-cuentas")
    public String gestionarCuentas(Model model,
                                   @RequestParam(required = false) String msg,
                                   @RequestParam(required = false) String err) {
        model.addAttribute("listaTipos", tipoSrv.listar());
        model.addAttribute("listaCuentas", cuentaSrv.listar());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/Gestionar-cuentas";
    }


    @PostMapping("/tipos-cuenta")
    public String crearTipo(@RequestParam String nombre, RedirectAttributes ra) {
        try {
            var t = new TipoCuenta();
            t.setNombre(nombre);
            tipoSrv.crear(t);
            ra.addAttribute("msg", "Tipo creado");
        } catch (Exception e) {
            ra.addAttribute("err", e.getMessage());
        }
        return "redirect:/admin/Gestionar-cuentas";
    }

    @PutMapping("/tipos-cuenta/{id}")
    public String actualizarTipo(@PathVariable Long id, @RequestParam String nombre, RedirectAttributes ra) {
        try {
            var t = new TipoCuenta();
            t.setNombre(nombre);
            tipoSrv.actualizar(id, t);
            ra.addAttribute("msg", "Tipo actualizado");
        } catch (Exception e) {
            ra.addAttribute("err", e.getMessage());
        }
        return "redirect:/admin/Gestionar-cuentas";
    }

    @DeleteMapping("/tipos-cuenta/{id}")
    public String eliminarTipo(@PathVariable Long id, RedirectAttributes ra) {
        try {
            tipoSrv.eliminar(id);
            ra.addAttribute("msg", "Tipo eliminado");
        } catch (Exception e) {
            ra.addAttribute("err", e.getMessage());
        }
        return "redirect:/admin/Gestionar-cuentas";
    }

    // -------- Cuentas contables --------
    @PostMapping("/cuentas-contables")
    public String crearCuenta(@RequestParam String codigo,
                              @RequestParam String nombre,
                              @RequestParam("tipoId") Long tipoId,
                              RedirectAttributes ra) {
        try {
            var c = new CuentaContable();
            c.setCodigo(codigo);
            c.setNombre(nombre);
            c.setTipo(new TipoCuenta(tipoId)); 
            cuentaSrv.crear(c);
            ra.addAttribute("msg", "Cuenta creada");
        } catch (Exception e) {
            ra.addAttribute("err", e.getMessage());
        }
        return "redirect:/admin/Gestionar-cuentas";
    }

    @PutMapping("/cuentas-contables/{id}")
    public String actualizarCuenta(@PathVariable Long id,
                                   @RequestParam String codigo,
                                   @RequestParam String nombre,
                                   @RequestParam("tipoId") Long tipoId,
                                   RedirectAttributes ra) {
        try {
            var c = new CuentaContable();
            c.setCodigo(codigo);
            c.setNombre(nombre);
            c.setTipo(new TipoCuenta(tipoId));
            cuentaSrv.actualizar(id, c);
            ra.addAttribute("msg", "Cuenta actualizada");
        } catch (Exception e) {
            ra.addAttribute("err", e.getMessage());
        }
        return "redirect:/admin/Gestionar-cuentas";
    }

    @DeleteMapping("/cuentas-contables/{id}")
    public String eliminarCuenta(@PathVariable Long id, RedirectAttributes ra) {
        try {
            cuentaSrv.eliminar(id);
            ra.addAttribute("msg", "Cuenta eliminada");
        } catch (Exception e) {
            ra.addAttribute("err", e.getMessage());
        }
        return "redirect:/admin/Gestionar-cuentas";
    }
}