package com.example.demo.tipocuenta;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-cuenta")
public class TipoCuentaController {

    private final TipoCuentaService service;

    public TipoCuentaController(TipoCuentaService service) { this.service = service; }

    @GetMapping
    public List<TipoCuenta> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public TipoCuenta get(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    public ResponseEntity<TipoCuenta> crear(@Validated @RequestBody TipoCuenta dto) {
        var saved = service.crear(dto);
        return ResponseEntity.created(URI.create("/api/tipos-cuenta/" + saved.getIdTipoCuenta())).body(saved);
    }

    @PutMapping("/{id}")
    public TipoCuenta actualizar(@PathVariable Long id, @Validated @RequestBody TipoCuenta dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
