package com.example.demo.detallepartida;

import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaContableRepository;
import com.example.demo.dto.DetallePartidaDTO;
import com.example.demo.partida.Partida;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePartidaService {

    private final DetallePartidaRepository repo;
    private final CuentaContableRepository cuentaRepo;

    public void guardarDetalle(Partida partida, DetallePartidaDTO dto) {

        CuentaContable cuenta = cuentaRepo.findById(dto.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + dto.getCuentaId()));

        DetallePartida det = new DetallePartida();
        det.setPartida(partida);
        det.setCuenta(cuenta);
        det.setDebe(dto.getDebe());
        det.setHaber(dto.getHaber());

        repo.save(det);
    }
}
