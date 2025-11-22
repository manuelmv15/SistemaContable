package com.example.demo.cuentacontable;

import com.example.demo.tipocuenta.TipoCuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaContableService {

    private final CuentaContableRepository repo;
    private final TipoCuentaRepository tipoRepo;

    public CuentaContableService(CuentaContableRepository repo, TipoCuentaRepository tipoRepo) {
        this.repo = repo;
        this.tipoRepo = tipoRepo;
    }

    public List<CuentaContable> listar() { return repo.findAll(); }

    public CuentaContable obtener(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Cuenta no existe"));
    }

    @Transactional
    public CuentaContable crear(CuentaContable c) {
        if (repo.existsByCodigo(c.getCodigo()))
            throw new IllegalArgumentException("Código de cuenta ya existe");
        var tipo = tipoRepo.findById(c.getTipo().getIdTipoCuenta())
                .orElseThrow(() -> new IllegalArgumentException("TipoCuenta inválido"));
        c.setTipo(tipo);
        return repo.save(c);
    }

    @Transactional
    public CuentaContable actualizar(Long id, CuentaContable c) {
        var db = obtener(id);
        if (!db.getCodigo().equals(c.getCodigo()) && repo.existsByCodigo(c.getCodigo()))
            throw new IllegalArgumentException("Código de cuenta ya existe");

        var tipo = tipoRepo.findById(c.getTipo().getIdTipoCuenta())
                .orElseThrow(() -> new IllegalArgumentException("TipoCuenta inválido"));

        db.setCodigo(c.getCodigo());
        db.setNombre(c.getNombre());
        db.setTipo(tipo);
        return repo.save(db);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<CuentaContable> porTipo(Long tipoId) {
        return repo.findByTipo_IdTipoCuentaOrderByCodigoAsc(tipoId);
    }

    public List<CuentaContable> buscar(Long periodoId, String q, String hasta) {
        return repo.findByNombreContainingIgnoreCaseOrderByCodigoAsc(q == null ? "" : q);
    }
}
