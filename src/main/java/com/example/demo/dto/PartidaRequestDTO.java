package com.example.demo.dto;

import java.util.List;

public class PartidaRequestDTO {
    private String fecha;      // "YYYY-MM-DD"
    private String concepto;
    private List<DetallePartidaDTO> detalles;

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public List<DetallePartidaDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePartidaDTO> detalles) { this.detalles = detalles; }
}
