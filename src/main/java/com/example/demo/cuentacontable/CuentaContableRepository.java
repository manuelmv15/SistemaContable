package com.example.demo.cuentacontable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaContableRepository extends JpaRepository<CuentaContable, Long> {
    Optional<CuentaContable> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    // Opción 1 (con el nombre real del id en TipoCuenta)
    List<CuentaContable> findByTipo_IdTipoCuentaOrderByCodigoAsc(Long idTipoCuenta);

    // Opción 2 (si prefieres pasar la entidad)
    // List<CuentaContable> findByTipoOrderByCodigoAsc(TipoCuenta tipo);

    List<CuentaContable> findByNombreContainingIgnoreCaseOrderByCodigoAsc(String q);
}
