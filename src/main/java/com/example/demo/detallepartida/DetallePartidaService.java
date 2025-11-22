package com.example.demo.detallepartida;

import com.example.demo.cuentacontable.CuentaRepository;
import com.example.demo.dto.DetallePartidaDTO;
import com.example.demo.partida.Partida;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePartidaService {

    private final DetallePartidaRepository detalleRepo;
    private final CuentaRepository cuentaRepo;

    public void guardarDetalle(Partida partida, DetallePartidaDTO dto) {

        var cuenta = cuentaRepo.findById(dto.getCuentaId().longValue())
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        DetallePartida det = new DetallePartida();
        det.setPartida(partida);
        det.setCuenta(cuenta);
        det.setDebe(dto.getDebe());
        det.setHaber(dto.getHaber());

        detalleRepo.save(det);
    }
}
