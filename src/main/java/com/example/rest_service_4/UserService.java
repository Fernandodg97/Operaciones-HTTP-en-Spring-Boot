package com.example.rest_service_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que implementa la lógica de negocio para la gestión de usuarios.
 * Esta clase actúa como intermediario entre el controlador y el repositorio de datos,
 * manejando las operaciones CRUD y las transformaciones de datos.
 * 
 * @author Your Name
 * @version 1.0
 */
@Service
public class UserService {

    /**
     * Repositorio de usuarios inyectado por Spring.
     * Proporciona acceso a las operaciones de base de datos.
     */
    @Autowired
    UserDAO userRepository;

    /**
     * Mapper de Jackson inyectado por Spring.
     * Utilizado para la conversión entre objetos Java y JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Recupera todos los usuarios de la base de datos.
     * 
     * @return Lista de todos los usuarios encontrados
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Busca un usuario por su identificador único.
     * 
     * @param id Identificador del usuario a buscar
     * @return Usuario encontrado o null si no existe
     */
    public User getUserbyId(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if(op.isPresent()) return op.get();
        else return null;
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     * 
     * @param user Usuario a crear
     * @return Usuario creado con su ID asignado
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Actualiza completamente un usuario existente.
     * Reemplaza todos los datos del usuario con los nuevos proporcionados.
     * 
     * @param id Identificador del usuario a actualizar
     * @param user Nuevos datos del usuario
     * @return Usuario actualizado o null si no existe
     */
    public User updateUser(Integer id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * Realiza una actualización parcial de un usuario usando JSON Patch.
     * Permite modificar campos específicos sin necesidad de enviar todos los datos.
     * 
     * @param id Identificador del usuario a actualizar
     * @param patch Operaciones de actualización en formato JSON Patch
     * @return Usuario actualizado o null si no existe
     * @throws JsonPatchException Si el formato del patch es inválido
     * @throws JsonProcessingException Si hay errores en el procesamiento JSON
     */
    public User patchUser(Integer id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            JsonNode patched = patch.apply(objectMapper.convertValue(user, JsonNode.class));
            User patchedUser = objectMapper.treeToValue(patched, User.class);
            patchedUser.setId(id);
            return userRepository.save(patchedUser);
        }
        return null;
    }

    /**
     * Elimina un usuario de la base de datos.
     * 
     * @param id Identificador del usuario a eliminar
     */
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
