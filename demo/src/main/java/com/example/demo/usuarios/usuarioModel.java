package com.example.demo.usuarios;

import com.example.demo.rol.rolModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_usuario_usuario", columnNames = "usuario")
})
public class usuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(name = "usuario", nullable = false, length = 50, unique = true)
    private String username;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tipo_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_rol")
    )
    private rolModel tipo; // FK → rol.id_rol

    @NotBlank
    @Size(max = 255)
    @JsonIgnore // evita exponer el hash en JSON
    @Column(name = "contrasenia", nullable = false, length = 255)
    private String contrasenia; // HASH (BCrypt)

    @NotNull
    @Column(name = "estado", nullable = false)
    private Boolean estado = Boolean.TRUE;

    public usuarioModel() {}

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public rolModel getTipo() { return tipo; }
    public void setTipo(rolModel tipo) { this.tipo = tipo; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
