package com.example.backend.rest;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.Role;
import com.example.backend.Repository.RoleRepository;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.user_management.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleRest {
    RoleRepository roleRepository;
    UserService userService;
    public RoleRest(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @PostMapping("/role/create")
    public ResponseEntity<Object> createRole(
            @RequestBody Role role,
            @RequestHeader(value = "X-API-KEY") String token
    ) {
        if (!userService.verifyApiKey(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (roleRepository.findByName(role.getName()) != null) return new ResponseEntity<>(new ErrorDto("a role with this name already exists"), HttpStatus.BAD_REQUEST);

        roleRepository.save(role);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/permissions")
    public ResponseEntity<Object> getAllPermissions(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) return new ResponseEntity<>(new ErrorDto("unauthorized"), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(Permissions.values(), HttpStatus.OK);
    }
}
