package com.example.demo.controllers;

import com.example.demo.clasificacionDocumentos.clasificacionDocumentoModel;
import com.example.demo.clasificacionDocumentos.clasificacionDocumentoService;
import com.example.demo.documentoFuente.documentoFuenteModel;
import com.example.demo.documentoFuente.documentoFuenteService;
import com.example.demo.perdiodoContable.PeriodoContableDTO;
import com.example.demo.perdiodoContable.PeriodoContableService;
import com.example.demo.rol.rolModel;
import com.example.demo.rol.rolRepository;
import com.example.demo.usuarios.usuarioRepository;
import com.example.demo.usuarios.usuarioService;
import org.springframework.dao.DataIntegrityViolationException;
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

    private final clasificacionDocumentoService clasificacionDocumentoService;
    private final documentoFuenteService documentoFuenteService;

    private final PeriodoContableService Periodoservice;

    @ModelAttribute("roles")
    public List<rolModel> roles() {
        return usuarioService.findAllRoles();
    }

    public adminController(usuarioService usuarioService,
                           rolRepository rolRepository,
                           usuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder, clasificacionDocumentoService clasificacionDocumentoService, documentoFuenteService documentoFuenteService,  PeriodoContableService periodoservice) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.clasificacionDocumentoService = clasificacionDocumentoService;
        this.documentoFuenteService = documentoFuenteService;
        this.Periodoservice = periodoservice;

    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }


    //--------------- clasificacion de documentos
    @GetMapping("/Clasificacion-Documentos")
    public String vista(@RequestParam(name = "clasificacionId", required = false) Long clasificacionId,
                        Model model) {

        List<clasificacionDocumentoModel> clasificaciones = clasificacionDocumentoService.findAll();
        List<documentoFuenteModel> documentos =
                (clasificacionId != null)
                        ? documentoFuenteService.findByClasificacion(clasificacionId)
                        : documentoFuenteService.findAll();

        model.addAttribute("listaClasificaciones", clasificaciones);
        model.addAttribute("listaDocumentos", documentos);
        model.addAttribute("filtroClasificacionId", clasificacionId);


        return "admin/Clasificacion-Documentos";
    }


    @PostMapping("/clasificaciones")
    public String crear(@RequestParam String nombre,
                        @RequestParam(required = false) String descripcion,
                        RedirectAttributes ra) {
        clasificacionDocumentoService.crear(nombre, descripcion);
        ra.addFlashAttribute("ok", "Clasificación creada correctamente.");
        return "redirect:/admin/Clasificacion-Documentos";
    }


    @PutMapping("/clasificaciones/{id}")
    public String editar(@PathVariable Long id,
                         @RequestParam String nombre,
                         @RequestParam(required = false) String descripcion,
                         RedirectAttributes ra) {
        clasificacionDocumentoService.editar(id, nombre, descripcion);
        ra.addFlashAttribute("ok", "Clasificación actualizada.");
        return "redirect:/admin/Clasificacion-Documentos";
    }


    @ExceptionHandler({IllegalArgumentException.class, DataIntegrityViolationException.class})
    public String handleClasifErrors(Exception ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:/admin/Clasificacion-Documentos";
    }




//------------------ periodos contables

    @GetMapping("/periodos")
    public String listar(Model model,
                         @RequestParam(value = "ok", required = false) String ok,
                         @RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "editId", required = false) Long editId) {
        model.addAttribute("periodos", Periodoservice.listar());
        model.addAttribute("ok", ok);
        model.addAttribute("error", error);

        PeriodoContableDTO dto = new PeriodoContableDTO();
        if (editId != null) {
            var p = Periodoservice.buscarPorId(editId);
            if (p != null) {
                dto.setId(p.getIdPeriodo());
                dto.setFechaInicio(p.getFechaInicio());
                dto.setFechaFin(p.getFechaFin());
            } else {
                model.addAttribute("error", "No se encontró el período con id " + editId);
            }
        }
        model.addAttribute("dto", dto);
        return "admin/Periodos-Contables";
    }

    @PostMapping("/periodos")
    public String guardar(@ModelAttribute("dto") PeriodoContableDTO dto,
                          RedirectAttributes rattrs) {
        try {
            if (dto.getId() == null) {
                Periodoservice.crear(dto.getFechaInicio(), dto.getFechaFin());
                rattrs.addAttribute("ok", "Período creado");
            } else {
                Periodoservice.actualizar(dto.getId(), dto.getFechaInicio(), dto.getFechaFin());
                rattrs.addAttribute("ok", "Período actualizado");
            }
        } catch (Exception e) {
            rattrs.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/periodos";
    }

    @PutMapping("/periodos/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute("dto") PeriodoContableDTO dto,
                             RedirectAttributes rattrs) {
        try {
            Periodoservice.actualizar(id, dto.getFechaInicio(), dto.getFechaFin());
            rattrs.addAttribute("ok", "Período actualizado");
        } catch (Exception e) {
            rattrs.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/periodos";
    }

    @DeleteMapping("/periodos/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes rattrs) {
        try {
            Periodoservice.eliminar(id);
            rattrs.addAttribute("ok", "Período eliminado");
        } catch (Exception e) {
            rattrs.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/periodos";
    }


    // auditorias ----------------

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





