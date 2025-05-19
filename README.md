# REST Service con Spring Boot

[Video explicativo: Operaciones HTTP en Spring Boot - Fernando Diaz](./Operaciones HTTP en Spring Boot Fernando Diaz.mp4)

Este proyecto es un servicio REST implementado con Spring Boot que proporciona operaciones CRUD para gestionar usuarios.

## Estructura del Proyecto

El proyecto sigue una arquitectura en capas:

```
src/main/java/com/example/rest_service_4/
├── RestService4Application.java    # Clase principal de Spring Boot
├── User.java                      # Entidad/Modelo de Usuario
├── UserDto.java                   # Objeto de Transferencia de Datos
├── UserDAO.java                   # Capa de Acceso a Datos
├── UserService.java               # Capa de Servicio
├── UserController.java            # Controlador de Lógica de Negocio
└── UserResource.java              # Controlador REST
```

## Componentes Principales

### UserResource (Controlador REST)
- Maneja las peticiones HTTP
- Define los endpoints REST
- Implementa las siguientes operaciones:
  - `GET /api/v0/users`: Obtiene todos los usuarios
  - `GET /api/v0/users/{id}`: Obtiene un usuario por ID
  - `POST /api/v0/users`: Crea un nuevo usuario
  - `PUT /api/v0/users/{id}`: Actualiza un usuario existente
  - `DELETE /api/v0/users/{id}`: Elimina un usuario

### UserController
- Implementa la lógica de negocio
- Convierte entre entidades y DTOs
- Métodos principales:
  - `getUsers()`: Obtiene lista de usuarios
  - `getUserById()`: Obtiene usuario por ID
  - `newUser()`: Crea nuevo usuario
  - `updateUser()`: Actualiza usuario existente
  - `remove()`: Elimina usuario

### UserService
- Implementa la lógica de negocio
- Interactúa con el repositorio de datos
- Métodos principales:
  - `getUsers()`: Obtiene todos los usuarios
  - `getUserbyId()`: Obtiene usuario por ID
  - `addUser()`: Añade nuevo usuario
  - `updateUser()`: Actualiza usuario existente
  - `deleteById()`: Elimina usuario

### UserDAO
- Interfaz de acceso a datos
- Extiende JpaRepository para operaciones CRUD básicas

### User (Entidad)
- Representa la entidad Usuario en la base de datos
- Contiene los atributos del usuario

### UserDto
- Objeto de Transferencia de Datos
- Usado para transferir datos entre capas
- Evita exponer la entidad directamente

## Endpoints REST

### Obtener todos los usuarios
```http
GET /api/v0/users
```

### Obtener usuario por ID
```http
GET /api/v0/users/{id}
```

### Crear nuevo usuario
```http
POST /api/v0/users
Content-Type: application/json

{
    "nombre": "Ejemplo",
    "email": "ejemplo@email.com"
}
```

### Actualizar usuario
```http
PUT /api/v0/users/{id}
Content-Type: application/json

{
    "nombre": "Nuevo Nombre",
    "email": "nuevo@email.com"
}
```

### Actualizar usuario parcialmente (PATCH)
```http
PATCH /api/v0/users/{id}
Content-Type: application/json-patch+json

[
    {
        "op": "replace",
        "path": "/nombre",
        "value": "Nuevo Nombre"
    },
    {
        "op": "add",
        "path": "/telefono",
        "value": "123456789"
    }
]
```

### Eliminar usuario
```http
DELETE /api/v0/users/{id}
```

## Tecnologías Utilizadas

- Spring Boot
- Spring Data JPA
- Maven
- Java

## Requisitos

- Java 17 o superior
- Maven
- Base de datos compatible con JPA

## Ejecución

1. Clonar el repositorio
2. Ejecutar `mvn spring-boot:run`
3. El servicio estará disponible en `http://localhost:4010`

## Implementación Detallada

### Implementación del PUT (Actualización de Usuario)

La funcionalidad de actualización de usuario se implementa en tres capas:

#### 1. UserResource (Capa de Presentación)
```java
@PutMapping("/{id}")
public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody UserDto user) {
    UserDto u = userController.updateUser(id, user);
    return ResponseEntity.ok().body(u);
}
```
- `@PutMapping("/{id}")`: Define el endpoint PUT con el ID como parámetro de ruta
- `@PathVariable Integer id`: Captura el ID del usuario a actualizar
- `@RequestBody UserDto user`: Recibe los datos actualizados del usuario
- Retorna un `ResponseEntity` con el usuario actualizado

#### 2. UserController (Capa de Control)
```java
public UserDto updateUser(Integer id, UserDto userDto) {
    User user = new User(userDto);
    return new UserDto(userService.updateUser(id, user));
}
```
- Convierte el DTO recibido a entidad User
- Llama al servicio para realizar la actualización
- Convierte la entidad actualizada de vuelta a DTO
- Maneja la transformación entre capas

#### 3. UserService (Capa de Servicio)
```java
public User updateUser(Integer id, User user) {
    Optional<User> existingUser = userRepository.findById(id);
    if (existingUser.isPresent()) {
        user.setId(id);
        return userRepository.save(user);
    }
    return null;
}
```
- Verifica si el usuario existe
- Si existe:
  - Asigna el ID al nuevo objeto usuario
  - Guarda los cambios en la base de datos
- Si no existe, retorna null

### Flujo de Datos en la Actualización

1. **Recepción de la Petición**:
   ```http
   PUT /api/v0/users/1
   Content-Type: application/json
   
   {
       "nombre": "Nuevo Nombre",
       "email": "nuevo@email.com"
   }
   ```

2. **Procesamiento**:
   - UserResource recibe la petición
   - Extrae el ID y los datos del cuerpo
   - Pasa los datos al UserController

3. **Transformación**:
   - UserController convierte UserDto a User
   - Mantiene la consistencia de los datos

4. **Persistencia**:
   - UserService verifica la existencia
   - Actualiza los datos en la base de datos
   - Retorna la entidad actualizada

5. **Respuesta**:
   - Los datos se convierten de vuelta a DTO
   - Se envía la respuesta al cliente

### Manejo de Errores

- Si el usuario no existe, se retorna null
- La capa de servicio maneja la lógica de negocio
- El controlador maneja la transformación de datos
- La capa de presentación maneja la respuesta HTTP

### Consideraciones de Implementación

1. **Idempotencia**:
   - El PUT es idempotente
   - Múltiples llamadas con los mismos datos producen el mismo resultado

2. **Validación**:
   - Se verifica la existencia del usuario
   - Se mantiene la integridad de los datos

3. **Transformación**:
   - Se mantiene la separación entre DTO y entidad
   - Se preserva la encapsulación de la capa de datos

4. **Respuesta**:
   - Se utiliza ResponseEntity para control HTTP
   - Se mantiene consistencia en el formato de respuesta

### Implementación del PATCH (Actualización Parcial)

La funcionalidad de actualización parcial se implementa usando JSON Patch (RFC 6902):

#### 1. UserResource (Capa de Presentación)
```java
@PatchMapping("/{id}")
public ResponseEntity<UserDto> patchUser(@PathVariable Integer id, @RequestBody JsonPatch patch) {
    try {
        UserDto u = userController.patchUser(id, patch);
        return ResponseEntity.ok().body(u);
    } catch (JsonPatchException | JsonProcessingException e) {
        return ResponseEntity.badRequest().build();
    }
}
```
- `@PatchMapping("/{id}")`: Define el endpoint PATCH
- Maneja las excepciones de JSON Patch y procesamiento JSON
- Retorna 400 Bad Request si el patch es inválido o hay errores de procesamiento

#### 2. UserController (Capa de Control)
```java
public UserDto patchUser(Integer id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
    User patchedUser = userService.patchUser(id, patch);
    return new UserDto(patchedUser);
}
```
- Pasa el patch al servicio
- Propaga las excepciones para su manejo en la capa superior
- Convierte la entidad actualizada a DTO

#### 3. UserService (Capa de Servicio)
```java
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
```
- Verifica si el usuario existe
- Aplica el patch al usuario existente
- Maneja las excepciones de procesamiento JSON
- Guarda los cambios en la base de datos

### Manejo de Errores en PATCH

1. **JsonPatchException**:
   - Ocurre cuando el formato del patch es inválido
   - Cuando las operaciones del patch no son válidas
   - Cuando las rutas especificadas no existen

2. **JsonProcessingException**:
   - Ocurre durante la conversión entre JSON y objetos Java
   - Durante la aplicación del patch
   - Durante la serialización/deserialización

3. **Respuestas HTTP**:
   - 200 OK: Patch aplicado correctamente
   - 400 Bad Request: Error en el formato o procesamiento del patch
   - 404 Not Found: Usuario no encontrado

### Operaciones JSON Patch Soportadas

1. **add**: Añade un nuevo valor
   ```json
   {
       "op": "add",
       "path": "/telefono",
       "value": "123456789"
   }
   ```

2. **remove**: Elimina un valor existente
   ```json
   {
       "op": "remove",
       "path": "/telefono"
   }
   ```

3. **replace**: Reemplaza un valor existente
   ```json
   {
       "op": "replace",
       "path": "/nombre",
       "value": "Nuevo Nombre"
   }
   ```

4. **copy**: Copia un valor de una ubicación a otra
   ```json
   {
       "op": "copy",
       "from": "/nombre",
       "path": "/alias"
   }
   ```

5. **move**: Mueve un valor de una ubicación a otra
   ```json
   {
       "op": "move",
       "from": "/nombre",
       "path": "/alias"
   }
   ```

6. **test**: Verifica si un valor existe
   ```json
   {
       "op": "test",
       "path": "/nombre",
       "value": "Nombre Actual"
   }
   ```

### Consideraciones del PATCH

1. **Idempotencia**:
   - El PATCH no es necesariamente idempotente
   - Depende de las operaciones específicas en el patch

2. **Validación**:
   - Se valida la sintaxis del JSON Patch
   - Se verifica la existencia del usuario
   - Se mantiene la integridad de los datos

3. **Seguridad**:
   - Se debe validar el contenido del patch
   - Se deben aplicar las políticas de seguridad adecuadas

4. **Rendimiento**:
   - El PATCH es más eficiente que PUT para actualizaciones parciales
   - Reduce el tráfico de red

## Autores
- [@Fernandodg97](https://github.com/Fernandodg97)

## Licencia

[CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es) 