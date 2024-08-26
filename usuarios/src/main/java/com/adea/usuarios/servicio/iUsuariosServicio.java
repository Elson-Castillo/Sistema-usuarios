package com.adea.usuarios.servicio;

import com.adea.usuarios.modelo.Usuario;

import java.util.List;
/**Autor Ing Elson Castillo
 * Interface del servicio metods a implementar del CRUD y la validacion de login
 */

public interface iUsuariosServicio {
    public List<Usuario> listarUsuarios();
    public void guardarUsuario(Usuario saveUsuario);
    public void borrarUsuario(Usuario deleteUsuario);
    public Usuario validarUsuario(String login, String password);
}
