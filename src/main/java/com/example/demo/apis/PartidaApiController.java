package com.example.demo.apis;

import com.example.demo.dto.PartidaRequestDTO;
import com.example.demo.partida.Partida;
import com.example.demo.partida.PartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidas")
@RequiredArgsConstructor
public class PartidaApiController {

    private final PartidaService partidaService;

    @PostMapping("/crear")
    public Partida crearPartida(@RequestBody PartidaRequestDTO dto) {
        return partidaService.guardarPartida(dto);
    }
}
