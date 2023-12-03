package com.example.backend.rest;

import com.example.backend.Repository.Role;
import com.example.backend.Repository.RoleRepository;
import com.example.backend.ResponseDtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleRest {
    RoleRepository roleRepository;
    public RoleRest(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping("/role/create")
    public ResponseEntity<Object> createRole(@RequestBody Role role) {
        if (roleRepository.findByName(role.getName()) != null) return new ResponseEntity<>(new ErrorDto("a role with this name already exists"), HttpStatus.BAD_REQUEST);

        roleRepository.save(role);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
