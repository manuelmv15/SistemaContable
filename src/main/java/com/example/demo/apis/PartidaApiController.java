package com.example.demo.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/partidas")
public class PartidaApiController {

    @Autowired
    private JdbcTemplate jdbc;

    @PostMapping("/registrar")
    public Map<String, Object> registrarPartida(@RequestBody Map<String, Object> body) {

        String fecha = body.get("fecha").toString();
        String concepto = body.get("concepto").toString();
        Integer usuarioId = Integer.parseInt(body.get("usuarioId").toString());

        // insertar partida
        String sqlPartida = """
            INSERT INTO partida (fecha, concepto, usuario_id)
            VALUES (?, ?, ?) RETURNING id_partida;
        """;

        Integer idPartida = jdbc.queryForObject(
                sqlPartida,
                Integer.class,
                fecha, concepto, usuarioId
        );

        // insertar detalles
        List<Map<String, Object>> detalles = (List<Map<String, Object>>) body.get("detalles");

        String sqlDetalle = """
           INSERT INTO detallepartida (partida_id, cuenta_id, debe, haber)
           VALUES (?, ?, ?, ?)
        """;

        for (Map<String, Object> d : detalles) {
            jdbc.update(
                    sqlDetalle,
                    idPartida,
                    Integer.parseInt(d.get("cuentaId").toString()),
                    Double.parseDouble(d.get("debe").toString()),
                    Double.parseDouble(d.get("haber").toString())
            );
        }

        return Map.of(
                "success", true,
                "idPartida", idPartida
        );
    }
}
