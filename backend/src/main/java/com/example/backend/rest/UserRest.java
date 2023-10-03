package com.example.backend.rest;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.ApiKeyDto;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.ResponseDtos.UserDto;
import com.example.backend.user_management.UserService;
import com.example.backend.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserRest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping("/login/public/auth")
    public ResponseEntity<Object> loginPublicAuth(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        UserInfoEntity userInfoEntity = userService.getUserByUsername(username);

        if (userInfoEntity == null) {
            return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(password, userInfoEntity.getPassword())) {
            String token = tokenUtils.generateToken();

            userService.updateToken(userInfoEntity.getId(), token);
            return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login/private/auth")
    public ResponseEntity<Object> loginPrivateAuth(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        String entryCode = requestBody.get("entryCode");

        UserInfoEntity userInfoEntity = userService.getUserByUsername(username);

        if (userInfoEntity == null) {
            return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(password, userInfoEntity.getPassword())) {
            String token = tokenUtils.generateToken();


            userService.updateToken(userInfoEntity.getId(), token);
            return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verify(@RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity userInfoEntity = userService.getUserByToken(token);

        if (userInfoEntity == null) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new UserDto(userInfoEntity.getUsername()), HttpStatus.OK);
    }
}
