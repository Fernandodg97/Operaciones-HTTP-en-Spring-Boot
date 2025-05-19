package com.example.rest_service_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * Controlador que maneja la lógica de negocio para las operaciones de usuarios.
 * Esta clase actúa como intermediario entre la capa de presentación (UserResource)
 * y la capa de servicio (UserService), manejando las transformaciones entre DTOs y entidades.
 * 
 * @author Your Name
 * @version 1.0
 */
@Controller
public class UserController {

    /**
     * Servicio de usuarios inyectado por Spring.
     * Proporciona acceso a las operaciones de base de datos.
     */
    @Autowired
    UserService userService;

    /**
     * Obtiene la lista de todos los usuarios.
     * Convierte las entidades User a UserDto para la capa de presentación.
     * 
     * @return Lista de UserDto con todos los usuarios
     */
    public List<UserDto> getUsers() {
        List<User> users = userService.getUsers();
        return users.stream().map(UserDto::new).toList();
    }

    /**
     * Obtiene un usuario específico por su ID.
     * 
     * @param id Identificador del usuario a buscar
     * @return UserDto con los datos del usuario encontrado
     */
    public UserDto getUserById(Integer id) {
        User user = userService.getUserbyId(id);
        return new UserDto(user);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * Convierte el DTO recibido a entidad antes de persistirlo.
     * 
     * @param userDto Datos del nuevo usuario
     * @return UserDto con los datos del usuario creado
     */
    public UserDto newUser(UserDto userDto) {
        User user = new User(userDto);
        return new UserDto(userService.addUser(user));
    }

    /**
     * Actualiza completamente un usuario existente.
     * Reemplaza todos los datos del usuario con los nuevos proporcionados.
     * 
     * @param id Identificador del usuario a actualizar
     * @param userDto Nuevos datos del usuario
     * @return UserDto con los datos actualizados
     */
    public UserDto updateUser(Integer id, UserDto userDto) {
        User user = new User(userDto);
        return new UserDto(userService.updateUser(id, user));
    }

    /**
     * Realiza una actualización parcial de un usuario usando JSON Patch.
     * Permite modificar campos específicos sin necesidad de enviar todos los datos.
     * 
     * @param id Identificador del usuario a actualizar
     * @param patch Operaciones de actualización en formato JSON Patch
     * @return UserDto con los datos actualizados
     * @throws JsonPatchException Si el formato del patch es inválido
     * @throws JsonProcessingException Si hay errores en el procesamiento JSON
     */
    public UserDto patchUser(Integer id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User patchedUser = userService.patchUser(id, patch);
        return new UserDto(patchedUser);
    }

    /**
     * Elimina un usuario del sistema.
     * 
     * @param id Identificador del usuario a eliminar
     */
    public void remove(Integer id) {
        userService.deleteById(id);
    }
}
