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

    //obtiene la lista de todos los tipos de cuentas
    @GetMapping
    public List<TipoCuenta> listar() { return service.listar(); }

    //obtiene un tipo de cuenta referenciado por su id
    @GetMapping("/{id}")
    public TipoCuenta get(@PathVariable Long id) { return service.obtener(id); }

    //crea un nuevo tipo de cuenta y retorna la entidad creada con su id
    @PostMapping
    public ResponseEntity<TipoCuenta> crear(@Validated @RequestBody TipoCuenta dto) {
        var saved = service.crear(dto);
        return ResponseEntity.created(URI.create("/api/tipos-cuenta/" + saved.getIdTipoCuenta())).body(saved);
    }

    //actualiza un tipo de cuenta en existencia
    //id-> identicador del tipo de cuenta que se va actualizar
    //dto-> datos actualizados del tipo de cuenta
    @PutMapping("/{id}")
    public TipoCuenta actualizar(@PathVariable Long id, @Validated @RequestBody TipoCuenta dto) {
        return service.actualizar(id, dto);
    }

    //elimina un tipo de cuenta en existencia
    //id-> identicador del tipo de cuenta que se va eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
