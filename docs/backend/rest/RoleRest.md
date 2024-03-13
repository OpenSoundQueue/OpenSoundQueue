# RoleRest.java Documentation 📄

`RoleRest.java` is a part of the backend system, specifically designed for handling all REST API endpoints related to roles. It interacts with the role-related data, providing functionalities such as creating, retrieving, editing, and deleting roles, as well as managing role permissions. This class is essential for the role management aspect of user access control within the application.

## Package 📦

```java
package com.example.backend.rest;
```

## Dependencies and Components 🛠️

- **Repositories**: Utilizes `RoleRepository` for CRUD operations related to roles.
- **Services**: Leverages `UserService` for operations related to user-role relationships.
- **Annotations**: Uses `@AuthRequest` for endpoint access control based on permissions.
- **Response Entities**: Employs `ErrorDto` for standardized error responses.

## Class Definition and Constructor 🏗️

```java
@RestController
@RequestMapping("/api")
public class RoleRest {

    RoleRepository roleRepository;
    UserService userService;

    public RoleRest(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }
}
```

## Endpoints 🌐

### Create Role

- **Endpoint**: `/api/role/create`
- **Method**: POST
- **Auth Required**: Yes (`MANAGE_ROLES`)
- **Description**: Creates a new role if it does not already exist.

### Get All Roles

- **Endpoint**: `/api/roles`
- **Method**: GET
- **Auth Required**: Yes (`MANAGE_ROLES`)
- **Description**: Retrieves all roles.

### Get Role by ID

- **Endpoint**: `/api/role/get/{id}`
- **Method**: GET
- **Auth Required**: Yes (`MANAGE_ROLES`)
- **Description**: Gets a specific role by its ID.

### Delete Role

- **Endpoint**: `/api/role/{id}`
- **Method**: DELETE
- **Auth Required**: Yes (`MANAGE_ROLES`)
- **Description**: Deletes a role by ID if it exists.

### Edit Role

- **Endpoint**: `/api/role/edit`
- **Method**: PATCH
- **Auth Required**: Yes (`MANAGE_ROLES`)
- **Description**: Edits an existing role, including its name, permissions, and members.

### Get All Permissions

- **Endpoint**: `/api/permissions`
- **Method**: GET
- **Auth Required**: Yes (`MANAGE_ROLES`)
- **Description**: Lists all available permissions excluding `NO_VALUE`.

## Error Handling 🚨

Uses `ErrorDto` to construct error messages for various failure scenarios such as:
- Role not found
- Role already exists with given name

## Summary 📋

`RoleRest.java` plays a crucial role in managing roles and permissions within the application. It ensures that only authorized users can perform specific operations related to role management, thus enforcing access control and enhancing security.

---

For more detailed documentation on each endpoint, including request and response formats, refer to the API documentation.