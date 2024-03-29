/**
 * all Rest endpoints related to users and permissions
 */

package com.example.backend.rest;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.Role;
import com.example.backend.Repository.RoleRepository;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.ApiKeyDto;
import com.example.backend.ResponseDtos.ErrorDto;
import com.example.backend.ResponseDtos.UserDto;
import com.example.backend.annotations.AuthRequest;
import com.example.backend.system_management.PropertyService;
import com.example.backend.system_management.SystemService;
import com.example.backend.user_management.UserService;
import com.example.backend.util.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRest {
    private final SystemService systemService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtils tokenUtils;
    private final PropertyService propertyService;
    private final RoleRepository roleRepository;

    public UserRest(SystemService systemService, UserService userService, PasswordEncoder passwordEncoder, TokenUtils tokenUtils, PropertyService propertyService, RoleRepository roleRepository) {
        this.systemService = systemService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtils = tokenUtils;
        this.propertyService = propertyService;
        this.roleRepository = roleRepository;
    }

    /**
     * login endpoint for when the privacy settings are on not private and email authentication on
     * @param requestBody
     * @return
     */
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

    /**
     * login endpoint for when the privacy settings are on private and email authentication off
     * @param requestBody
     * @return
     * @throws IOException
     */
    @PostMapping("/login/private")
    public ResponseEntity<Object> loginPrivate(@RequestBody Map<String, String> requestBody) throws IOException {
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

    /**
     * login endpoint for when the privacy settings are on private and email authentication on
     * @param requestBody
     * @return
     * @throws IOException
     */
    @PostMapping("/login/private/auth")
    public ResponseEntity<Object> loginPrivateAuth(@RequestBody Map<String, String> requestBody) throws IOException {
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

    /**
     * login endpoint for when the privacy settings are on not private and email authentication off
     * @param requestBody
     * @return
     */
    @PostMapping("/login/public")
    public ResponseEntity<Object> loginPublic(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");

        UserInfoEntity userInfoEntity = userService.getUserByUsername(username);

        if (userInfoEntity != null) {
            if (userService.getAllOnlineUsers().stream().map(UserDto::getUsername).toList().contains(userInfoEntity.getUsername())) {
                return new ResponseEntity<>(new ErrorDto("Username already in use"), HttpStatus.BAD_REQUEST);
            } else {
                userService.deleteUser(userInfoEntity.getId());
            }
        }

        userInfoEntity = userService.registerNewUser(new UserInfoEntity(username));

        String token = tokenUtils.generateToken();

        userService.updateToken(userInfoEntity.getId(), token);
        userService.updateLastOnline(userInfoEntity);

        return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
    }

    /**
     * log current user out
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity user = userService.getUserByToken(token);

        userService.removeToken(user);
        userService.updateLastOnline(user);


        return ResponseEntity.ok().build();
    }

    /**
     * verify that the given token is valid
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest
    @GetMapping("/verify/api-key")
    public ResponseEntity<Object> verifyApiKey(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));

        return ResponseEntity.ok().build();
    }

    /**
     * verify that the given entry code is valid
     * @param requestBody
     * @return
     * @throws IOException
     */
    @GetMapping("/verify/entry-code")
    public ResponseEntity<Object> verifyEntryCode(@RequestBody Map<String, String> requestBody) throws IOException {
        String entryCode = requestBody.get("entry-code");

        if (systemService.checkEntryCode(entryCode)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(new ErrorDto("Invalid entry key"), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * get all users that are registered in the system
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_USER)
    @GetMapping("/users")
    public ResponseEntity<Object> users(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));

        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * get a certain user by their id
     * @param id
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_USER)
    @GetMapping("/user/get/{id}")
    public ResponseEntity<Object> getUser(@PathVariable(name = "id") Long id, @RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("User with id " + id + " does not exist"), HttpStatus.BAD_REQUEST);
        }

        userService.updateLastOnline(userService.getUserByToken(token));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * delete a certain user by their id
     * @param id
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_USER)
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") Long id, @RequestHeader(value = "X-API-KEY") String token) {
        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(new ErrorDto("User with id " + id + " does not exist"), HttpStatus.BAD_REQUEST);
        }

        if (userService.getUserByToken(token).equals(userService.getUserById(id))) {
            return new ResponseEntity<>(new ErrorDto("You can not delete your own user"), HttpStatus.BAD_REQUEST);
        }

        userService.updateLastOnline(userService.getUserByToken(token));

        userService.deleteUser(id);

        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * get data of the user that sent this request
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest
    @GetMapping("/self")
    public ResponseEntity<Object> self(@RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity user = userService.getUserByToken(token);
        userService.updateLastOnline(user);

        return new ResponseEntity<>(new UserDto(user.getId(), user.getUsername(), user.getLastOnline()), HttpStatus.OK);
    }

    /**
     * create a new user
     * @param user
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    @PostMapping("/register/create-account")
    public ResponseEntity<Object> createAccount(@RequestBody UserInfoEntity user) throws MessagingException, IOException {
        UserInfoEntity savedUser;
        if (userService.getUserByUsername(user.getUsername()) != null && userService.getUserByUsername(user.getUsername()).getVerified()) return new ResponseEntity<>(new ErrorDto("username is already taken"), HttpStatus.BAD_REQUEST);
        if (userService.getUserByEmail(user.getEmail()) != null) {
            if (userService.getUserByEmail(user.getEmail()).getVerified()) {
                return ResponseEntity.badRequest().body(new ErrorDto("Email address is already taken"));
            } else {
                savedUser = userService.getUserByEmail(user.getEmail());
                userService.updateName(savedUser.getId(), user.getUsername());
                savedUser.setUsername(user.getUsername());
            }
        } else {
            if (userService.getUserByUsername(user.getUsername()) == null) {
                savedUser = userService.registerNewAuthUser(user);
                if (!Boolean.parseBoolean(propertyService.getProperty("system.installed"))) {
                    userService.changeRolesOfUser(savedUser.getId(), List.of(roleRepository.findByName("Owner")));
                }
            } else {
                savedUser = userService.getUserByUsername(user.getUsername());
                userService.updateEmail(savedUser.getId(), user.getEmail());
                savedUser.setEmail(user.getEmail());
            }
        }

        userService.sendEmailVerification(savedUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * verify the email authentication code for a registration
     * @param input
     * @return
     */
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

    /**
     * resend a authentication email for a registration
     * @param input
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    @PostMapping("/register/resend-email")
    public ResponseEntity<Object> resendEmail(@RequestBody Map<String, String> input) throws MessagingException, IOException {
        String email = input.get("email");

        if (userService.getUserByEmail(email) == null) return new ResponseEntity<>(new ErrorDto("email does not exist"), HttpStatus.BAD_REQUEST);
        if (userService.getUserByEmail(email).getVerified()) return new ResponseEntity<>(new ErrorDto("email is already verified"), HttpStatus.BAD_REQUEST);

        userService.sendEmailVerification(userService.getUserByEmail(email));

        return ResponseEntity.ok().build();
    }

    /**
     * change the roles of the given id's user
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param id
     * @param roles
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.MANAGE_USER)
    @PatchMapping("/user/{id}/roles")
    public ResponseEntity<Object> changeRolesOfUser(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "id") Long id, @RequestBody List<Role> roles) {
        if (userService.getUserById(id) == null) return new ResponseEntity<>(new ErrorDto("user with id not found"), HttpStatus.BAD_REQUEST);

        userService.changeRolesOfUser(id, roles);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * get the permissions of the given user
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest
    @GetMapping("/user/permissions")
    public ResponseEntity<Object> getPermissionsOfUser(@RequestHeader(value = "X-API-KEY") String token) {
        if (userService.getUserByToken(token) == null) return new ResponseEntity<>(new ErrorDto("user with token not found"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(userService.getPermissionsOfUser(userService.getUserByToken(token).getId()), HttpStatus.CREATED);
    }
}
