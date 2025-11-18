package com.example.demo.tipocuenta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipocuenta")
@Getter @Setter
public class TipoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipoCuenta")
    private Long idTipoCuenta;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;


    public TipoCuenta() {}

    public TipoCuenta(Long idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }
}
