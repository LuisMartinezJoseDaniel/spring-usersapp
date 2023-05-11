# Backend en Spring Boot para el CRUD de Usuarios y Login Utilizando Spring Security y JWT

## Requisitos

- Java 8
- Maven
- MySQL
- Postman
- IDE (Eclipse, IntelliJ, NetBeans, etc)
- Git

## Instalación

1. Clonar el repositorio
2. Crear una base de datos en MySQL con el nombre que db_users_springboot
3. Modificar el archivo application.properties con los datos de conexión a la base de datos
4. Correr el proyecto con el comando mvn spring-boot:run
5. Probar los endpoints con Postman

## Endpoints

### Crear un usuario

```
[POST] /users
```

### Obtener todos los usuarios

```
[GET] /users
```

### Obtener un usuario por ID

```
[GET] /users/{id}
```

### Actualizar un usuario

```
[PUT] /users/{id}
```

### Eliminar un usuario

```
[DELETE] /users/{id}
```

### Login

```
[POST] /login
```

