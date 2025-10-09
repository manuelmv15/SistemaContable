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

    // ✅ Constructor vacío obligatorio para JPA
    public TipoCuenta() {}

    // ✅ Constructor opcional para cuando solo quieres referenciar por ID
    public TipoCuenta(Long idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }
}
