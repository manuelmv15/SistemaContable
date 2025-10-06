package com.example.demo.auth;

import com.example.demo.usuarios.usuarioModel;

public class LoginResponse {
    public Long id;
    public String nombre;
    public String email;
    public String username;
    public String rol;
    public boolean estado;

    public static LoginResponse from(usuarioModel u) {
        var r = new LoginResponse();
        r.id = u.getId();
        r.nombre = u.getNombre();
        r.email = u.getEmail();
        r.username = u.getUsername();
        r.rol = (u.getTipo() != null) ? u.getTipo().getNombre() : null;
        r.estado = Boolean.TRUE.equals(u.getEstado());
        return r;
    }
}
