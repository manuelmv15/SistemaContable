package com.example.demo.dto;

public class DetallePartidaDTO {
    private Long cuentaId;
    private Double debe;
    private Double haber;

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }
    public Double getDebe() { return debe; }
    public void setDebe(Double debe) { this.debe = debe; }
    public Double getHaber() { return haber; }
    public void setHaber(Double haber) { this.haber = haber; }
}
