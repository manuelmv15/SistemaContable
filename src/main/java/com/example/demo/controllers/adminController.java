package com.example.demo.controllers;

import com.example.demo.rol.rolModel;
import com.example.demo.rol.rolRepository;
import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import com.example.demo.usuarios.usuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final usuarioService usuarioService;
    private final rolRepository rolRepository;
    private final usuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("roles")
    public List<rolModel> roles() {
        return usuarioService.findAllRoles();
    }

    public adminController(usuarioService usuarioService,
                          rolRepository rolRepository,
                          usuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
    public String gestionarUsuarios(Model model){
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("roles", usuarioService.findAllRoles());
        return "admin/Gestionar-Usuarios";
    }

    @PostMapping("/usuarios") //
    public String crearUsuario(@RequestParam String nombre,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam Long rolId,
                               RedirectAttributes ra){
        usuarioService.crearUsuario(nombre, email, username, rolId);
        ra.addFlashAttribute("ok", "Usuario creado.");
        return "redirect:/admin/Gestionar-Usuarios";
    }


    @PostMapping("/usuarios/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra){
        usuarioService.toggleEstado(id);
        ra.addFlashAttribute("ok", "Estado de usuario actualizado.");
        return "redirect:/admin/Gestionar-Usuarios";
    }
}





