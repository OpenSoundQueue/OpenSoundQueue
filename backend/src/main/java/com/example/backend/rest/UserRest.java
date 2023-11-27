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

import javax.mail.MessagingException;
import java.io.IOException;
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

        if (!userInfoEntity.getVerified()) {
            return new ResponseEntity<>(new ErrorDto("Email address is not verified yet"), HttpStatus.BAD_REQUEST);
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

        if (!userInfoEntity.getVerified()) {
            return new ResponseEntity<>(new ErrorDto("Email address is not verified yet"), HttpStatus.BAD_REQUEST);
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

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        UserInfoEntity user = userService.getUserByToken(token);

        userService.removeToken(user);
        userService.updateLastOnline(user);


        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify/api-key")
    public ResponseEntity<Object> verifyApiKey(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        userService.updateLastOnline(userService.getUserByToken(token));

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

        userService.updateLastOnline(userService.getUserByToken(token));

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

        userService.updateLastOnline(userService.getUserByToken(token));

        userService.deleteUser(id);

        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/self")
    public ResponseEntity<Object> self(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        UserInfoEntity user = userService.getUserByToken(token);
        userService.updateLastOnline(user);

        return new ResponseEntity<>(new UserDto(user.getId(), user.getUsername(), user.getLastOnline()), HttpStatus.OK);
    }

    @PostMapping("/register/create-account")
    public ResponseEntity<Object> createAccount(@RequestBody UserInfoEntity user) throws MessagingException, IOException {
        UserInfoEntity savedUser;
        if (userService.getUserByUsername(user.getUsername()) != null ) return new ResponseEntity<>(new ErrorDto("username is already taken"), HttpStatus.BAD_REQUEST);
        if (userService.getUserByEmail(user.getEmail()) != null) {
            if (userService.getUserByEmail(user.getEmail()).getVerified()) {
                return ResponseEntity.badRequest().body(new ErrorDto("Email address is already taken"));
            } else {
                savedUser = userService.getUserByEmail(user.getEmail());
                savedUser.setUsername(user.getUsername());
                savedUser.setPassword(user.getPassword());
                userService.deleteUser(savedUser.getId());
                savedUser = userService.registerNewAuthUser(savedUser);
            }
        } else {
            savedUser = userService.registerNewAuthUser(user);
        }

        userService.sendEmailVerification(savedUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register/verify")
    public ResponseEntity<Object> verifyAccount(@RequestBody Map<String, String> input) {
        String code = input.get("code");
        String email = input.get("email");

        boolean verified = userService.verifyEmail(email, code);

        if (!verified) return new ResponseEntity<>(new ErrorDto("verification code is wrong"), HttpStatus.BAD_REQUEST);

        UserInfoEntity user = userService.getUserByEmail(email);

        String token = tokenUtils.generateToken();

        userService.updateToken(user.getId(), token);
        userService.updateLastOnline(user);

        return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.CREATED);
    }

    @PostMapping("/register/resend-email")
    public ResponseEntity<Object> resendEmail(@RequestBody Map<String, String> input) throws MessagingException, IOException {
        String email = input.get("email");

        if (userService.getUserByEmail(email) == null) return new ResponseEntity<>(new ErrorDto("email does not exist"), HttpStatus.BAD_REQUEST);
        if (userService.getUserByEmail(email).getVerified()) return new ResponseEntity<>(new ErrorDto("email is already verified"), HttpStatus.BAD_REQUEST);

        userService.sendEmailVerification(userService.getUserByEmail(email));

        return ResponseEntity.ok().build();
    }
}
