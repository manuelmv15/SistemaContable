package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class PartidaRequestDTO {
    private String fecha;
    private String concepto;
    private Long usuarioId;
    private List<DetallePartidaDTO> detalles;
}
