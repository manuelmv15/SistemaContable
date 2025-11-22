package com.example.demo.partida;

import com.example.demo.dto.DetallePartidaDTO;
import com.example.demo.dto.PartidaRequestDTO;
import com.example.demo.detallepartida.DetallePartida;
import com.example.demo.detallepartida.DetallePartidaRepository;
import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaRepository;
import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepo;
    private final DetallePartidaRepository detalleRepo;
    private final CuentaRepository cuentaRepo;
    private final usuarioRepository usuarioRepo;

    @Transactional
    public Partida crearPartidaDesdeDTO(PartidaRequestDTO dto, String username) {
        if (dto == null) throw new IllegalArgumentException("Payload vacío");

        // Obtener usuario autenticado por username
        usuarioModel usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + username));

        // Convertir fecha
        java.sql.Date fechaSql;
        try {
            fechaSql = java.sql.Date.valueOf(dto.getFecha());
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha inválida (use YYYY-MM-DD): " + dto.getFecha());
        }

        // Crear y guardar partida
        Partida p = new Partida();
        p.setFecha(fechaSql);
        p.setConcepto(dto.getConcepto());
        p.setUsuario(usuario);

        Partida guardada = partidaRepo.save(p);

        // Guardar detalles
        if (dto.getDetalles() != null) {
            for (DetallePartidaDTO d : dto.getDetalles()) {
                if (d == null) continue;
                // Validar que exista la cuenta
                CuentaContable cuenta = cuentaRepo.findById(d.getCuentaId())
                        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + d.getCuentaId()));

                // Si ambos cero, saltar
                double debe = d.getDebe() != null ? d.getDebe() : 0.0;
                double haber = d.getHaber() != null ? d.getHaber() : 0.0;
                if (debe == 0.0 && haber == 0.0) continue;

                DetallePartida detalle = new DetallePartida();
                detalle.setPartida(guardada);
                detalle.setCuenta(cuenta);
                detalle.setDebe(debe);
                detalle.setHaber(haber);

                detalleRepo.save(detalle);
            }
        }

        return guardada;
    }
}
