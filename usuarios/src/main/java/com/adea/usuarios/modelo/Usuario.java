package com.adea.usuarios.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**Autor Ing Elson Castillo
 * Clase de entidad
 */


@Entity
@Data
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NUM_REGISTRO", updatable = false, nullable = false)
    private Integer numRegistro;

    @Column(name = "LOGIN", nullable = false, length = 20)
    private String login;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "FECHAALTA", nullable = false)
    private LocalDate fechaAlta = LocalDate.now();

    @Column(name = "FECHABAJA")
    private LocalDate fechaBaja;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String status;

    @Column(name = "INTENTOS", nullable = false)
    private Float intentos = 0.0f;

    @Column(name = "FECHAREVOCADO")
    private LocalDate fechaRevocado;

    @Column(name = "FECHA_VIGENCIA")
    private LocalDate fechaVigencia;

    @Column(name = "NO_ACCESO")
    private Integer noAcceso;

    @Column(name = "APELLIDO_PATERNO", length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50)
    private String apellidoMaterno;

    @Column(name = "AREA")
    private Integer area;

    @Column(name = "FECHAMODIFICACION", nullable = false)
    private LocalDate fechaModificacion = LocalDate.now();

    // Getters y Setters automaticos con lombook
    // ...
}
