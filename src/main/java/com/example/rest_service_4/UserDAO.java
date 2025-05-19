package com.example.rest_service_4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de acceso a datos para la entidad User.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas.
 * Esta interfaz es responsable de la persistencia de los datos de usuarios en la base de datos.
 * 
 * @author Your Name
 * @version 1.0
 */
@Repository
public interface UserDAO extends JpaRepository<User,Integer> {
    
    /**
     * Recupera todos los usuarios de la base de datos.
     * 
     * @return Lista de todos los usuarios encontrados
     */
    List<User> findAll();

    /**
     * Busca un usuario por su identificador único.
     * 
     * @param id Identificador del usuario a buscar
     * @return Optional que contiene el usuario si existe, o vacío si no se encuentra
     */
    Optional<User> findById(@Param("id") Integer id);

    /**
     * Guarda o actualiza un usuario en la base de datos.
     * Si el usuario no existe, se crea uno nuevo.
     * Si el usuario existe, se actualiza con los nuevos datos.
     * 
     * @param user Usuario a guardar o actualizar
     * @return Usuario guardado con su ID asignado
     */
    User save(User user);
}
