package com.example.demo.apis;

import com.example.demo.dto.PartidaRequestDTO;
import com.example.demo.partida.Partida;
import com.example.demo.partida.PartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PartidaApiController {

    private final PartidaService partidaService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody PartidaRequestDTO dto) {
        Partida p = partidaService.crearDesdeDTO(dto);
        return ResponseEntity.ok(p);
    }

    // opcional: listar partidas
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(partidaService);
    }
}
