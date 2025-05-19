package com.example.rest_service_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * Controlador REST que maneja las peticiones HTTP para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD y actualizaciones parciales usando JSON Patch.
 * 
 * @author Your Name
 * @version 1.0
 */
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {
    /**
     * Ruta base para todos los endpoints de usuarios.
     * Define la versión de la API (v0) y el recurso (users).
     */
    public static final String USERS = "/api/v0/users";

    /**
     * Controlador de lógica de negocio inyectado por Spring.
     * Maneja las operaciones de negocio y la transformación de datos.
     */
    @Autowired
    UserController userController;

    /**
     * Obtiene la lista de todos los usuarios.
     * 
     * @return ResponseEntity con la lista de usuarios y código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userController.getUsers();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Obtiene un usuario específico por su ID.
     * 
     * @param id Identificador del usuario a buscar
     * @return ResponseEntity con el usuario encontrado y código 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        UserDto u = userController.getUserById(id);
        return ResponseEntity.ok().body(u);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * 
     * @param user Datos del nuevo usuario en formato DTO
     * @return ResponseEntity con el usuario creado y código 200 OK
     */
    @PostMapping
    public ResponseEntity<UserDto> newUser(@RequestBody UserDto user) {
        UserDto u = userController.newUser(user);
        return ResponseEntity.ok().body(u);
    }

    /**
     * Actualiza completamente un usuario existente.
     * Reemplaza todos los datos del usuario con los nuevos proporcionados.
     * 
     * @param id Identificador del usuario a actualizar
     * @param user Nuevos datos del usuario en formato DTO
     * @return ResponseEntity con el usuario actualizado y código 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody UserDto user) {
        UserDto u = userController.updateUser(id, user);
        return ResponseEntity.ok().body(u);
    }

    /**
     * Realiza una actualización parcial de un usuario usando JSON Patch.
     * Permite modificar campos específicos sin necesidad de enviar todos los datos.
     * 
     * @param id Identificador del usuario a actualizar
     * @param patch Operaciones de actualización en formato JSON Patch
     * @return ResponseEntity con el usuario actualizado y código 200 OK,
     *         o código 400 Bad Request si el patch es inválido
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@PathVariable Integer id, @RequestBody JsonPatch patch) {
        try {
            UserDto u = userController.patchUser(id, patch);
            return ResponseEntity.ok().body(u);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un usuario del sistema.
     * 
     * @param id Identificador del usuario a eliminar
     * @return ResponseEntity con código 200 OK y cuerpo vacío
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        userController.remove(id);
        return ResponseEntity.ok().body(null);
    }
}
