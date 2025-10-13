package com.example.demo.tipocuenta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_cuenta")
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
