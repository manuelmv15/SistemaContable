package com.example.demo.usuarios;

import jakarta.persistence.*;
import com.example.demo.rol.rolModel;

@Entity
@Table(name = "usuario")
public class usuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombre;
    private String email;

    @Column(name = "usuario", nullable = false, unique = true)
    private String username;

    @Column(name = "contrasenia", nullable = false, length = 255)
    private String contrasenia;

    @Column(name = "estado", nullable = false)
    private Boolean estado = Boolean.TRUE;

    // ⬇️ Relación al rol usando la columna tipo_id como FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tipo_id",
            referencedColumnName = "id_rol",
            foreignKey = @ForeignKey(name = "fk_usuario_rol")
    )
    private rolModel rol;

    // getters/setters mínimos
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public rolModel getRol() { return rol; }
    public void setRol(rolModel rol) { this.rol = rol; }
}
