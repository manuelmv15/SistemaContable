package com.example.demo.cuentacontable;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaContableController {

    private final CuentaContableService service;

    public CuentaContableController(CuentaContableService service) {
        this.service = service;
    }

    // GET /api/cuentas?periodoId=&hasta=&q=&tipoId=
    @GetMapping
    public List<CuentaContable> listar(
            @RequestParam(required = false) Long periodoId,
            @RequestParam(required = false) String hasta,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long tipoId
    ) {
        if (tipoId != null) {
            return service.porTipo(tipoId);          // ajusta si tu firma es distinta
        }
        if (periodoId != null || q != null || hasta != null) {
            return service.buscar(periodoId, q, hasta); // ajusta si tu firma es distinta
        }
        return service.listar();                     // lista todo
    }

    @GetMapping("/{id}")
    public CuentaContable get(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<CuentaContable> crear(@Validated @RequestBody CuentaContable dto) {
        var saved = service.crear(dto);
        return ResponseEntity
                .created(URI.create("/api/cuentas/" + saved.getIdCuenta()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public CuentaContable actualizar(@PathVariable Long id,
                                     @Validated @RequestBody CuentaContable dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
