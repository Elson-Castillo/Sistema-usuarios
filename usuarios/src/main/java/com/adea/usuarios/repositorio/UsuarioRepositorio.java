package com.adea.usuarios.repositorio;

import com.adea.usuarios.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Autor Ing Elson Castillo Interface del repositorio
 */
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    // Método para encontrar un usuario por su login y password
    @Query("SELECT u FROM Usuario u WHERE u.login = :login AND u.password = :password")
    public Usuario findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.status = :status")
    public void actualizarStatus(@Param("status") String status);

    // Método para obtener la fecha de vigencia de un usuario por su login
    @Query("SELECT u.fechaVigencia FROM Usuario u WHERE u.login = :login")
    public LocalDate findFechaVigenciaByLogin(@Param("login") String login);

    // Método para verificar si un login ya existe en la base de datos
    public boolean existsByLogin(String login);
}
