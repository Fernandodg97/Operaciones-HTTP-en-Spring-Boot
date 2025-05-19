package com.example.rest_service_4;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Clase que representa la entidad Usuario en la base de datos.
 * Esta clase está mapeada a la tabla "users" en la base de datos.
 * Utiliza JPA para el mapeo objeto-relacional y Lombok para reducir el código boilerplate.
 * 
 * @author Your Name
 * @version 1.0
 */
@Data
@Entity
@Table(name = "users")
public class User {
    
    /**
     * Identificador único del usuario.
     * Este campo es la clave primaria de la tabla.
     */
    @Id
    private Integer id;

    /**
     * Correo electrónico del usuario.
     * Debe ser único en el sistema.
     */
    private String email;

    /**
     * Nombre completo del usuario.
     * Se mapea a la columna "full_name" en la base de datos.
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * Contraseña del usuario.
     * En una implementación real, debería estar encriptada.
     */
    private String password;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public User(){};

    /**
     * Constructor con todos los campos.
     * 
     * @param id Identificador del usuario
     * @param email Correo electrónico del usuario
     * @param fullName Nombre completo del usuario
     * @param password Contraseña del usuario
     */
    public User(Integer id, String email, String fullName, String password) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    /**
     * Constructor que crea un User a partir de un UserDto.
     * Este método se utiliza para convertir entre la capa de presentación y la capa de datos.
     * 
     * @param userDto Objeto DTO con los datos del usuario
     */
    public User(UserDto userDto) {
        id = userDto.getId();
        email = userDto.getEmail();
        fullName = userDto.getFullName();
        password = userDto.getPassword();
    }
}
