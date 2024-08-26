package com.adea.usuarios.servicio;

import com.adea.usuarios.modelo.Usuario;
import com.adea.usuarios.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

/**
 * Autor Ing Elson Castillo Clase de servicio que implementa todos lo metodos
 * del CRUD de la intefaces servico y repositorio
 */
@Service
public class UsuarioServicio implements iUsuariosServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public void guardarUsuario(Usuario saveUsuario) {
        usuarioRepositorio.save(saveUsuario);
    }

    @Override
    public void borrarUsuario(Usuario deleteUsuario) {
        usuarioRepositorio.delete(deleteUsuario);
    }

    @Transactional
    public void actualizarStatusUsuario(String status) {
        usuarioRepositorio.actualizarStatus(status);
    }

    public LocalDate obtenerFechaVigenciaPorLogin(String login) {
        return usuarioRepositorio.findFechaVigenciaByLogin(login);
    }

    // Método para verificar si un login ya existe en la base de datos
    public boolean existeLogin(String login) {
        return usuarioRepositorio.existsByLogin(login);
    }

    // Método para validar credenciales del usuario
    @Override
    public Usuario validarUsuario(String login, String password) {
        //Se encripta la contraseña como se almacenó en la base de datos
        String encryptedPassword = toBase64(sha1(password));
        return usuarioRepositorio.findByLoginAndPassword(login, encryptedPassword);
    }

    // Métodos para encriptar la contraseña usando SHA-1 y codificarla en Base64
    private String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] sha1(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
