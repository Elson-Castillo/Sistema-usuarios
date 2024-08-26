package com.adea.usuarios.controlador;

import com.adea.usuarios.modelo.Usuario;
import com.adea.usuarios.servicio.UsuarioServicio;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * Autor Ing Elson Castillo Clase de controller del login
 */
@Data
@SessionScoped
@Component
public class LoginControlador {

    @Autowired
    UsuarioServicio usuarioServicio;
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    private String usuario;
    private String password;
    private LocalDate fechaLocal;

    private String login;
    private String contrasena;
    private String nombre;
    private String paterno;
    private String materno;
    private String status;
    private LocalDate fechaVence;

    //se crea un metodo con una instancia de la entidad usuario settear sus atributos
    public void agregarUsuario() {

        this.usuarioSeleccionado = new Usuario();
        this.usuarioSeleccionado.setLogin(this.login);
        this.usuarioSeleccionado.setPassword(this.toBase64(sha1(this.contrasena)));
        this.usuarioSeleccionado.setNombre(this.nombre);
        this.usuarioSeleccionado.setApellidoPaterno(this.paterno);
        this.usuarioSeleccionado.setApellidoMaterno(this.materno);
        this.usuarioSeleccionado.setStatus(this.status);
        this.usuarioSeleccionado.setFechaVigencia(this.fechaVence);
    }

    public void cargarDatos() {
        this.usuarios = new LinkedList<>();
    }

    //Metodo de guaradr
    public void guardarUsuario() {

        this.agregarUsuario();
        this.cargarDatos();

        // Primero, verifica si el login ya existe en la base de datos
        boolean loginExistente = usuarioServicio.existeLogin(login);

        if (loginExistente == false) {

            //Si esta condicion se cumple guarda
            if (this.usuarioSeleccionado.getNumRegistro() == null) {
                this.usuarioServicio.guardarUsuario(this.usuarioSeleccionado);
                this.usuarios.add(this.usuarioSeleccionado);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Aviso!", "Usuario registrado con exito");
                PrimeFaces.current().dialog().showMessageDynamic(message);
                //Ocultamos la ventana y refrescamos con ajax
                PrimeFaces.current().executeScript("PF('ventanaModalUser').hide();");
                PrimeFaces.current().ajax().update("forma-user:mensajes", "forma-user:user-tabla");
                this.limpiarFormulario();
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "¡Error!", "El ID_LOGIN: \"" + login + "\",  ya está registrado, favor de probar con otro");
            PrimeFaces.current().dialog().showMessageDynamic(message);
        }
    }

    // Método para limpiar el formulario
    public void limpiarFormulario() {
        this.usuarioSeleccionado = null;
        this.usuarios.clear();
        this.login = "";
        this.contrasena = "";
        this.nombre = "";
        this.paterno = "";
        this.materno = "";
        this.status = "";
        this.fechaVence = null;
    }

    public String validarLogin() throws IOException {
        // Verifica si el login ya existe en la base de datos
        boolean loginExistente = usuarioServicio.existeLogin(usuario);

        if (loginExistente) {

            //Obtiene la fecha vigencia de la BD
            LocalDate fechaVigencia = usuarioServicio.obtenerFechaVigenciaPorLogin(usuario);
            //Compara la fecha vigencia de la BD con la fecha actual para validar el Login
            if (fechaVigencia == null) {
                //Si pasa hace la validacion del login con usuario y contraseña
                Usuario usuarioValidado = usuarioServicio.validarUsuario(usuario, password);
                if (usuarioValidado != null) {
                    // Login exitoso, redirigir a página de bienvenida
                    FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
                    return null;
                } else {
                    // Mostrar mensaje de error usuario y contraseña incorrectos
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡Error!", "Usuario o contraseña incorrectos");
                    PrimeFaces.current().dialog().showMessageDynamic(message);
                    return null; // Permanece en la página de login
                }
            } else {
                if (fechaVigencia.isAfter(fechaLocal = LocalDate.now())) {
                    //Si pasa hace la validacion del login con usuario y contraseña
                    Usuario usuarioValidado = usuarioServicio.validarUsuario(usuario, password);
                    if (usuarioValidado != null) {
                        // Login exitoso, redirigir a página de bienvenida
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Aviso!", "Bienvenido: \"" + usuario + "\"");
                        PrimeFaces.current().dialog().showMessageDynamic(message);
                        PrimeFaces.current().executeScript("setTimeout(function() { window.location.href = 'index.xhtml'; }, 4000);");
                        return null;
                    } else {
                        // Mostrar mensaje de error fecha vegencia caduco
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "¡Error!", "Contraseña incorrecta, intente de nuevo por favor.");
                        PrimeFaces.current().dialog().showMessageDynamic(message);
                        return null;
                    }
                } else {
                    // Mostrar mensaje de error fecha vegencia caduco
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡Error!", "No puede iniciar sesion la fecha vigencia caduco");
                    PrimeFaces.current().dialog().showMessageDynamic(message);
                    return null;
                }

            }
        } else {
            // Mostrar mensaje de error 
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡Error!", "No se encontro registro de este usuario en la base de datos");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return null;
        }
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

    // Getters y Setters para de los atributos generados de manera automatica
}
