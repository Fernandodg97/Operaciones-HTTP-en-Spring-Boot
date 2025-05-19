package com.example.rest_service_4;

import lombok.Data;

/**
 * Clase DTO (Data Transfer Object) para la entidad User.
 * Se utiliza para transferir datos entre la capa de presentación y la capa de servicio.
 * Esta clase evita exponer directamente la entidad User y proporciona una capa de abstracción.
 * 
 * @author Your Name
 * @version 1.0
 */
@Data
public class UserDto {
    /**
     * Identificador único del usuario.
     * Corresponde con el ID de la entidad User.
     */
    Integer id;

    /**
     * Correo electrónico del usuario.
     * Debe ser único en el sistema.
     */
    String email;

    /**
     * Nombre completo del usuario.
     * Corresponde con el campo fullName de la entidad User.
     */
    String fullName;

    /**
     * Contraseña del usuario.
     * En una implementación real, debería estar encriptada.
     * No se debería exponer en las respuestas HTTP.
     */
    String password;

    /**
     * Constructor por defecto.
     * Requerido para la deserialización JSON.
     */
    public UserDto(){}

    /**
     * Constructor que crea un DTO a partir de una entidad User.
     * Este método se utiliza para convertir entre la capa de datos y la capa de presentación.
     * 
     * @param user Entidad User de la que se copian los datos
     */
    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        fullName = user.getFullName();
        password = user.getPassword();
    }
}
