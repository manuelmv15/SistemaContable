package com.example.demo.clasificacionDocumentos;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class clasificacionDocumentoService {

    private final clasificacionDocumentoRepository repo;

    public clasificacionDocumentoService(clasificacionDocumentoRepository repo) { this.repo = repo; }

    public List<clasificacionDocumentoModel> findAll() {
        return repo.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
    }

    @Transactional
    public clasificacionDocumentoModel crear(String nombre, String descripcion) {
        if (repo.existsByNombreIgnoreCase(nombre.trim())) {
            throw new IllegalArgumentException("Ya existe una clasificación con ese nombre.");
        }
        var c = new clasificacionDocumentoModel();
        c.setNombre(nombre.trim());
        c.setDescripcion(descripcion);
        try {
            return repo.save(c);
        } catch (DataIntegrityViolationException e) {
            // por si el unique de DB salta
            throw new IllegalArgumentException("Nombre de clasificación duplicado.");
        }
    }

    @Transactional
    public void editar(Long id, String nombre, String descripcion) {
        var c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Clasificación no existe."));
        c.setNombre(nombre.trim());
        c.setDescripcion(descripcion);
        // Hibernate flushea al final de la transacción
    }
}
