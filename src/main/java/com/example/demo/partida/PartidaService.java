package com.example.demo.partida;

import com.example.demo.dto.DetallePartidaDTO;
import com.example.demo.dto.PartidaRequestDTO;
import com.example.demo.detallepartida.DetallePartidaService;
import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepo;
    private final DetallePartidaService detalleService;
    private final usuarioRepository usuarioRepo;

    @Transactional
    public Partida crearDesdeDTO(PartidaRequestDTO dto) {
        // validar fecha
        java.sql.Date fechaSql;
        try {
            fechaSql = java.sql.Date.valueOf(dto.getFecha());
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha inválida. Use formato YYYY-MM-DD");
        }

        usuarioModel usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getUsuarioId()));

        Partida p = new Partida();
        p.setFecha(fechaSql);
        p.setConcepto(dto.getConcepto());
        p.setUsuario(usuario);

        Partida guardada = partidaRepo.save(p);

        if (dto.getDetalles() != null) {
            for (DetallePartidaDTO det : dto.getDetalles()) {
                detalleService.guardarDetalle(guardada, det);
            }
        }

        return guardada;
    }
}
