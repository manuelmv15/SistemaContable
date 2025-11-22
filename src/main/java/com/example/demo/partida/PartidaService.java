package com.example.demo.partida;

import com.example.demo.dto.DetallePartidaDTO;
import com.example.demo.dto.PartidaRequestDTO;
import com.example.demo.detallepartida.DetallePartidaService;
import com.example.demo.usuarios.usuarioModel;
import com.example.demo.usuarios.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final DetallePartidaService detalleService;
    private final usuarioRepository userRepo;

    public Partida guardarPartida(PartidaRequestDTO dto) {

        usuarioModel usuario = userRepo.findById(dto.getUsuarioId().longValue())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Partida partida = new Partida();
        partida.setFecha(java.sql.Date.valueOf(dto.getFecha()));
        partida.setConcepto(dto.getConcepto());
        partida.setUsuario(usuario);

        Partida guardada = partidaRepository.save(partida);

        for (DetallePartidaDTO det : dto.getDetalles()) {
            detalleService.guardarDetalle(guardada, det);
        }

        return guardada;
    }
}
