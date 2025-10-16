package com.example.demo.usuarios;

import com.example.demo.rol.rolModel;
import com.example.demo.rol.rolRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class usuarioController {

    private final usuarioRepository usuarioRepo;
    private final rolRepository rolRepo;

    public usuarioController(usuarioRepository usuarioRepo, rolRepository rolRepo) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
    }

    @GetMapping
    public List<usuarioModel> listar() {
        return usuarioRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<usuarioModel> obtener(@PathVariable Long id) {
        return usuarioRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody usuarioModel u) {

        Long rolId = (u.getRol() != null) ? u.getRol().getIdRol() : null;
        if (rolId == null) return ResponseEntity.badRequest().body("Debe especificar tipo.id (rol).");

        rolModel rol = rolRepo.findById(rolId).orElse(null);
        if (rol == null) return ResponseEntity.badRequest().body("El rol especificado no existe.");

        u.setRol(rol);
        return ResponseEntity.ok(usuarioRepo.save(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody usuarioModel datos) {
        return usuarioRepo.findById(id).map(u -> {
            u.setNombre(datos.getNombre());
            u.setEmail(datos.getEmail());
            u.setUsername(datos.getUsername());
            u.setEstado(datos.getEstado());

            if (datos.getRol() != null && datos.getRol().getIdRol() != null) {
                rolRepo.findById(datos.getRol().getIdRol()).ifPresent(u::setRol);
            }

            return ResponseEntity.ok(usuarioRepo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!usuarioRepo.existsById(id)) return ResponseEntity.notFound().build();
        usuarioRepo.deleteById(id);
        return ResponseEntity.ok("Eliminado");
    }
}
