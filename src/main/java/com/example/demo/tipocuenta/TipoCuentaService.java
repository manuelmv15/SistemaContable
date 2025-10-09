package com.example.demo.tipocuenta;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoCuentaService {

    private final TipoCuentaRepository repo;

    public TipoCuentaService(TipoCuentaRepository repo) {
        this.repo = repo;
    }

    public List<TipoCuenta> listar() { return repo.findAll(); }

    public TipoCuenta obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("TipoCuenta no existe"));
    }

    @Transactional
    public TipoCuenta crear(TipoCuenta t) {
        if (repo.existsByNombreIgnoreCase(t.getNombre()))
            throw new IllegalArgumentException("Nombre de tipo ya existe");
        return repo.save(t);
    }

    @Transactional
    public TipoCuenta actualizar(Long id, TipoCuenta t) {
        var db = obtener(id);
        if (!db.getNombre().equalsIgnoreCase(t.getNombre())
                && repo.existsByNombreIgnoreCase(t.getNombre())) {
            throw new IllegalArgumentException("Nombre de tipo ya existe");
        }
        db.setNombre(t.getNombre());
        return repo.save(db);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
