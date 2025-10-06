package com.example.demo.rol;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class rolController {

    private final rolRepository repo;

    public rolController(rolRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<rolModel> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<rolModel> obtener(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody rolModel rol) {
        if (repo.existsByNombre(rol.getNombre())) {
            return ResponseEntity.badRequest().body("El nombre de rol ya existe.");
        }
        return ResponseEntity.ok(repo.save(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody rolModel datos) {
        return repo.findById(id).map(r -> {
            r.setNombre(datos.getNombre());
            return ResponseEntity.ok(repo.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok("Eliminado");
    }
}
