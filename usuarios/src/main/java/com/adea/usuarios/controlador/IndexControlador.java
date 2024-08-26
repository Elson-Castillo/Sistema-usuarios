package com.adea.usuarios.controlador;

/**
 * Autor Ing Elson Castillo Clase de controlador del index
 */
import com.adea.usuarios.modelo.Usuario;
import com.adea.usuarios.servicio.UsuarioServicio;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Data
@SessionScoped
@Component
public class IndexControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    private LocalDate filtroFechaAlta;
    
    private boolean editableLogin;
    
    public void prepararEdicion() {
        this.editableLogin = false; // Hacer que el campo login no sea editable en modo edición
    }

//listamos los datos para hacer un select de la infromacion almacenada
    @PostConstruct
    public void init() {
        this.cargarDatos();
    }
    
    public void cargarDatos() {
        this.usuarios = usuarioServicio.listarUsuarios();
    }

    //se crea un metodo con una instancia de la entidad usuario para acceder a sus atributos
    public void agregarUsuario() {
        this.editableLogin = true;
        this.usuarioSeleccionado = new Usuario();
    }

    //Metodos de guaradr y actualizar
    public void guardarUsuario() {
        // Primero, verifica si el login ya existe en la base de datos
        boolean loginExistente = usuarioServicio.existeLogin(this.usuarioSeleccionado.getLogin());
        
        this.usuarioSeleccionado.setPassword(this.toBase64(sha1(this.usuarioSeleccionado.getPassword())));

        //Si esta condicion se cumple guarda
        if (this.usuarioSeleccionado.getNumRegistro() == null) {
            if (loginExistente == false) {
                
                this.usuarioServicio.guardarUsuario(this.usuarioSeleccionado);
                this.usuarios.add(this.usuarioSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aviso", "Usuario agregado correctamente"));
                //Ocultamos la ventana y refrescamos con ajax
                PrimeFaces.current().executeScript("PF('ventanaModalUser').hide();");
                PrimeFaces.current().ajax().update("forma-user:mensajes", "forma-user:user-tabla");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "¡Error!", "El ID_LOGIN: \"" + this.usuarioSeleccionado.getLogin() + "\",  ya está registrado, favor de probar con otro");
                PrimeFaces.current().dialog().showMessageDynamic(message);
            }
        } else {//De  lo contrario en el else actualiza
            this.usuarioServicio.guardarUsuario(this.usuarioSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aviso", "Usuario actualizado correctamente"));
            //Ocultamos la ventana y refrescamos con ajax
            PrimeFaces.current().executeScript("PF('ventanaModalUser').hide();");
            PrimeFaces.current().ajax().update("forma-user:mensajes", "forma-user:user-tabla");
        }
        
    }

    //Metodo de borra cuenta de usuario
    public void eliminarCuenta() {
        this.usuarioServicio.borrarUsuario(this.usuarioSeleccionado);
        this.usuarios.remove(this.usuarioSeleccionado);
        this.usuarioSeleccionado = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aviso", "Usuario eliminado correctamente"));
        PrimeFaces.current().ajax().update("forma-user:mensajes", "forma-user:user-tabla");
    }

    // Método para cambiar el estado del usuario
    public void cambiarEstadoUsuario(String nuevoEstado) {
        for (Usuario usuario : usuarios) {
            usuario.setStatus(nuevoEstado);
            this.usuarioServicio.actualizarStatusUsuario(nuevoEstado);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aviso", "Status actualizado correctamente"));
        PrimeFaces.current().ajax().update("forma-user:mensajes", "forma-user:user-tabla");
    }

    // Métodos para encriptar la contraseña usando SHA-1 y codificarla en Base64 para que asi se guearde el BD
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
