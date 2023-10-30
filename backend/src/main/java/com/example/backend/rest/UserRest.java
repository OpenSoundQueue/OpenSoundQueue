package com.example.backend.rest;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.ApiKeyDto;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.ResponseDtos.UserDto;
import com.example.backend.system_management.SystemService;
import com.example.backend.user_management.UserService;
import com.example.backend.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRest {

    @Autowired
    private SystemService systemService;

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
            userService.updateLastOnline(userInfoEntity);
            return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login/private")
    public ResponseEntity<Object> loginPrivate(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String entryCode = requestBody.get("entryCode");

        UserInfoEntity userInfoEntity = userService.getUserByUsername(username);

       if (!systemService.checkEntryCode(entryCode)) {
            return new ResponseEntity<>(new ErrorDto("Invalid entry key"), HttpStatus.UNAUTHORIZED);
        }

        if (userInfoEntity != null) {
            return new ResponseEntity<>(new ErrorDto("Username already in use"), HttpStatus.BAD_REQUEST);
        }

        userInfoEntity = userService.registerNewUser(new UserInfoEntity(username));

        String token = tokenUtils.generateToken();
        userService.updateToken(userInfoEntity.getId(), token);
        userService.updateLastOnline(userInfoEntity);

        return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
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

        if (!systemService.checkEntryCode(entryCode)) {
            return new ResponseEntity<>(new ErrorDto("Invalid entry key"), HttpStatus.UNAUTHORIZED);
        }

        if (passwordEncoder.matches(password, userInfoEntity.getPassword())) {
            String token = tokenUtils.generateToken();

            userService.updateToken(userInfoEntity.getId(), token);
            userService.updateLastOnline(userInfoEntity);

            return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login/public")
    public ResponseEntity<Object> loginPublic(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");

        UserInfoEntity userInfoEntity = userService.getUserByUsername(username);

        if (userInfoEntity != null) {
            return new ResponseEntity<>(new ErrorDto("Username already in use"), HttpStatus.BAD_REQUEST);
        }

        userInfoEntity = userService.registerNewUser(new UserInfoEntity(username));

        String token = tokenUtils.generateToken();

        userService.updateToken(userInfoEntity.getId(), token);
        userService.updateLastOnline(userInfoEntity);

        return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
    }

    @GetMapping("/verify/api-key")
    public ResponseEntity<Object> verifyApiKey(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify/entry-code")
    public ResponseEntity<Object> verifyEntryCode(@RequestBody Map<String, String> requestBody) {
        String entryCode = requestBody.get("entry-code");

        if (systemService.checkEntryCode(entryCode)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(new ErrorDto("Invalid entry key"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> users(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") Long id, @RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        if (userService.getUserByToken(token).equals(userService.getUserById(id))) {
            return new ResponseEntity<>(new ErrorDto("You can not delete your own user"), HttpStatus.BAD_REQUEST);
        }

        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(new ErrorDto("User with id " + id + " does not exist"), HttpStatus.BAD_REQUEST);
        }

        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/self")
    public ResponseEntity<Object> self(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        UserInfoEntity user = userService.getUserByToken(token);

        return new ResponseEntity<>(new UserDto(user.getId(), user.getUsername(), user.getLastOnline()), HttpStatus.OK);
    }
}
