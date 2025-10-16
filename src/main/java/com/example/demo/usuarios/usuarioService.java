package com.example.demo.usuarios;

import com.example.demo.rol.rolModel;
import com.example.demo.rol.rolRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class usuarioService {


    private final usuarioRepository usuarioRepository;
    private final rolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.user.default-password:123456}")
    private String defaultPassword;

    public usuarioService(usuarioRepository usuarioRepository,
                          rolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<usuarioModel> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<usuarioModel> findByUsername(String username) {

        return usuarioRepository.findByUsername(username);
    }



    public List<rolModel> findAllRoles() {
        return rolRepository.findAll();
    }

    @Transactional
    public void crearUsuario(String nombre, String email, String username, Long rolId) {
        var rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido"));

        var u = new usuarioModel();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setUsername(username);
        u.setEstado(true);
        u.setContrasenia(passwordEncoder.encode(defaultPassword));
        u.setRol(rol);

        usuarioRepository.save(u);
    }

    @Transactional
    public void toggleEstado(Long id){
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
        u.setEstado(Boolean.FALSE.equals(u.getEstado()));
    }

    @Transactional
    public void cambiarPassword(Long idUsuario, String nueva) {
        var u = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
        u.setContrasenia(passwordEncoder.encode(nueva));

    }
    @Transactional
    public void cambiarPasswordPorUsername(String username, String nueva) {
        var u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
        u.setContrasenia(passwordEncoder.encode(nueva));
    }


}
