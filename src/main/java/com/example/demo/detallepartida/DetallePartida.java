package com.example.demo.detallepartida;

import com.example.demo.partida.Partida;
import com.example.demo.cuentacontable.CuentaContable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detallepartida")
@Getter @Setter
public class DetallePartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private CuentaContable cuenta;

    private Double debe;
    private Double haber;
}
