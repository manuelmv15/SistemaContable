package com.example.demo.partida;

import com.example.demo.dto.PartidaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contador")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping("/registrar-partida")
    public String registrar(@RequestBody PartidaRequestDTO dto) {
        partidaService.guardarPartida(dto);
        return "Partida guardada correctamente";
    }
}
