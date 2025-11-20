package com.example.demo.detallepartida;

import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaContableRepository;
import com.example.demo.dto.DetallePartidaDTO;
import com.example.demo.partida.Partida;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DetallePartidaService {

    private final DetallePartidaRepository detalleRepo;
    private final CuentaContableRepository cuentaRepo;

    @Transactional
    public void guardarDetalle(Partida partida, DetallePartidaDTO dto) {
        CuentaContable cuenta = cuentaRepo.findById(dto.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + dto.getCuentaId()));

        DetallePartida d = new DetallePartida();
        d.setPartida(partida);
        d.setCuenta(cuenta);
        d.setDebe(dto.getDebe() != null ? dto.getDebe() : 0.0);
        d.setHaber(dto.getHaber() != null ? dto.getHaber() : 0.0);

        detalleRepo.save(d);
    }
}
