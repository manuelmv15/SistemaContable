package com.example.demo.rol;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "rol", uniqueConstraints = {
        @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre")
})
public class rolModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL en PostgreSQL
    @Column(name = "id_rol")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    public rolModel() {}
    public rolModel(String nombre) { this.nombre = nombre; }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
