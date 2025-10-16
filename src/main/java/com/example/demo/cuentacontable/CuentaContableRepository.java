package com.example.demo.cuentacontable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaContableRepository extends JpaRepository<CuentaContable, Long> {
    Optional<CuentaContable> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    List<CuentaContable> findByTipo_IdTipoCuentaOrderByCodigoAsc(Long idTipoCuenta);

    List<CuentaContable> findByNombreContainingIgnoreCaseOrderByCodigoAsc(String q);
}
