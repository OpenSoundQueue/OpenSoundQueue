package com.example.backend.rest;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.Role;
import com.example.backend.Repository.RoleRepository;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.annotations.AuthRequest;
import com.example.backend.user_management.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class RoleRest {
    RoleRepository roleRepository;
    UserService userService;
    public RoleRest(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_ROLES)
    @PostMapping("/role/create")
    public ResponseEntity<Object> createRole(
            @RequestBody Role role,
            @RequestHeader(value = "X-API-KEY") String token
    ) {
        if (roleRepository.findByName(role.getName()) != null) return new ResponseEntity<>(new ErrorDto("a role with this name already exists"), HttpStatus.BAD_REQUEST);

        roleRepository.save(role);

        return new ResponseEntity<>(roleRepository.findByName(role.getName()),HttpStatus.CREATED);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_ROLES)
    @GetMapping("/roles")
    public ResponseEntity<Object> getAllRoles(@RequestHeader(value = "X-API-KEY") String token) {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_ROLES)
    @GetMapping("/role/get/{id}")
    public ResponseEntity<Object> getRole(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "id") int id) {
        if (roleRepository.findById(id).orElse(null) == null) return new ResponseEntity<>(new ErrorDto("role with id '" + id + "' not found"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(roleRepository.findById(id), HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_ROLES)
    @DeleteMapping("/role/{id}")
    public ResponseEntity<Object> deleteRole(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "id") int id) {
        if (roleRepository.findById(id).orElse(null) == null) return new ResponseEntity<>(new ErrorDto("role with id '" + id + "' not found"), HttpStatus.BAD_REQUEST);
        userService.removeRole(id);
        roleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_ROLES)
    @PatchMapping("/role/edit")
    public ResponseEntity<Object> editRole(
            @RequestBody Role role,
            @RequestHeader(value = "X-API-KEY") String token
    ) {
        if (roleRepository.findById(role.getId()).orElse(null) == null) return new ResponseEntity<>(new ErrorDto("Role with this id does not exist"), HttpStatus.BAD_REQUEST);
        if (roleRepository.findByName(role.getName()) != null && roleRepository.findByName(role.getName()).getId() != role.getId()) return new ResponseEntity<>(new ErrorDto("a role with this name already exists"), HttpStatus.BAD_REQUEST);

        Role savedRole = roleRepository.findById(role.getId()).get();

        savedRole.setName(role.getName());
        savedRole.setPermissions(role.getPermissions());

        roleRepository.save(savedRole);

        return new ResponseEntity<>(savedRole,HttpStatus.CREATED);
    }

    @AuthRequest(requiredPermission = Permissions.MANAGE_ROLES)
    @GetMapping("/permissions")
    public ResponseEntity<Object> getAllPermissions(@RequestHeader(value = "X-API-KEY") String token) {
        return new ResponseEntity<>(Arrays.stream(Permissions.values()).filter(x -> x != Permissions.NO_VALUE).toArray(), HttpStatus.OK);
    }
}
