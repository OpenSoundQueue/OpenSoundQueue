/**
 * This service is responsible for all logic related to users
 */

package com.example.backend.user_management;

import com.example.backend.Repository.*;
import com.example.backend.ResponseDtos.UserDto;
import com.example.backend.email.EmailComponent;
import com.example.backend.email.EmailUtils;
import com.example.backend.util.TokenUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserService {
    private UserInfoRepository userInfoRepository;
    private PasswordEncoder passwordEncoder;
    private TokenUtils tokenUtils;
    private EmailUtils emailUtils;
    private EmailComponent emailComponent;

    RoleRepository roleRepository;

    private Map<String, String> emailVerificationCodes = new HashMap<>();

    public UserService(
            UserInfoRepository userInfoRepository,
            PasswordEncoder passwordEncoder,
            TokenUtils tokenUtils,
            EmailUtils emailUtils,
            EmailComponent emailComponent,
            RoleRepository roleRepository
    ) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtils = tokenUtils;
        this.emailUtils = emailUtils;
        this.emailComponent = emailComponent;
        this.roleRepository = roleRepository;
    }

    /**
     * get user by username
     * @param username
     * @return
     */
    public UserInfoEntity getUserByUsername(String username) {
        return userInfoRepository.findByUsername(username);
    }

    /**
     * get user by token
     * @param token
     * @return
     */
    public UserInfoEntity getUserByToken(String token) {
        return userInfoRepository.findByToken(tokenUtils.hashWithSHA512(token));
    }

    /**
     * get uesr by id
     * @param id
     * @return
     */
    public UserInfoEntity getUserById(Long id) {
        return userInfoRepository.findById(id).orElse(null);
    }

    /**
     * get user by email
     * @param email
     * @return
     */
    public UserInfoEntity getUserByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    /**
     * verify that a given access token is valid
     * @param token the token that is being checked
     * @return boolean
     */
    public boolean verifyApiKey(String token) {
        UserInfoEntity user = getUserByToken(token);

        if (user == null) {
            return false;
        }

        updateLastOnline(user);

        return true;
    }

    /**
     * creating a user with password
     * @param user
     * @return
     */
    public UserInfoEntity registerNewAuthUser(UserInfoEntity user) {
        String clearTextPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(clearTextPassword));

        user = userInfoRepository.save(user);

        changeRolesOfUser(user.getId(), List.of(roleRepository.findById(1).get()));

        return user;
    }

    /**
     * creating a user without password
     * @param user
     * @return
     */
    public UserInfoEntity registerNewUser(UserInfoEntity user) {
        user = userInfoRepository.save(user);
        changeRolesOfUser(user.getId(), List.of(roleRepository.findById(1).get()));
        return user;
    }

    /**
     * change access token of a given user
     * @param id
     * @param token
     */
    public void updateToken(Long id, String token) {
        UserInfoEntity user = userInfoRepository.findById(id).orElse(null);

        if (user == null) {
            return;
        }

        user.setToken(tokenUtils.hashWithSHA512(token));

        userInfoRepository.save(user);
    }

    /**
     * make a certain access token invalid
     * @param user
     */
    public void removeToken(UserInfoEntity user) {
        user.setToken(null);
        userInfoRepository.save(user);
    }

    /**
     * set the timestamp for the last action that has been performed by a given user
     * @param user
     */
    public void updateLastOnline(UserInfoEntity user) {
        user.setLastOnline(new Date());
        userInfoRepository.save(user);
    }

    /**
     * returns all users
     * @return a list of users
     */
    public List<UserDto> getAll() {
        List<UserDto> allUsers = userInfoRepository.findAll().stream().map(userInfoEntity -> new UserDto(userInfoEntity.getId(), userInfoEntity.getUsername(), userInfoEntity.getLastOnline())).toList();

        return allUsers;
    }

    /**
     * delete a user
     * @param id
     */
    public void deleteUser(Long id) {
        changeRolesOfUser(id, new LinkedList<>());
        userInfoRepository.deleteById(id);
    }

    /**
     * send an email for verifying a user's account
     * @param user
     * @throws MessagingException
     * @throws IOException
     */
    public void sendEmailVerification(UserInfoEntity user) throws MessagingException, IOException {
        String emailCode = emailUtils.generateEmailCode();
        System.out.println(emailCode);
        emailVerificationCodes.put(user.getEmail(), emailCode);
        emailComponent.sendMail(user.getEmail(), emailCode, user.getUsername());
    }

    /**
     * verifying the code that was sent in a verification email
     * @param email
     * @param code
     * @return
     */
    public boolean verifyEmail(String email, String code) {
        if (!emailVerificationCodes.containsKey(email)) return false;
        boolean verified = emailVerificationCodes.get(email).equals(code);

        if (verified) {
            emailVerificationCodes.remove(email);
            UserInfoEntity user = userInfoRepository.findByEmail(email);
            user.setVerified(true);
            userInfoRepository.save(user);
        }

        return verified;
    }

    /**
     * change roles of a user
     * @param id
     * @param roles
     */
    public void changeRolesOfUser(long id, List<Role> roles) {
        UserInfoEntity user = userInfoRepository.findById(id).get();
        List<Role> savedRoles = new ArrayList<>();

        System.out.println(roles);

        for (Role r :roles) {
            if (roleRepository.findById(r.getId()).orElse(null) == null) continue;
            savedRoles.add(roleRepository.findById(r.getId()).get());
        }

        user.setRoles(savedRoles);

        userInfoRepository.save(user);

        for (Role r :roleRepository.findAll()) {
            r.setMembers(userInfoRepository.findAllByRolesContains(r));
            roleRepository.save(r);
        }
    }

    /**
     * get a list of all permission of a user
     * @param id
     * @return
     */
    public List<Permissions> getPermissionsOfUser(long id) {
        UserInfoEntity user = userInfoRepository.findById(id).get();
        Set<Permissions> permissions = new HashSet<>();

        for (Role r :user.getRoles()) {
            permissions.addAll(r.getPermissions());
        }

        return permissions.stream().toList();
    }

    /**
     * remove a certain role
     * @param id
     */
    public void removeRole(int id) {
        for (UserInfoEntity u : userInfoRepository.findAll()) {
            if (!u.getRoles().stream().filter(x -> x.getId() == id).toList().isEmpty()) {
                changeRolesOfUser(u.getId(), u.getRoles().stream().filter(x -> x.getId() != id).toList());
            }
        }
    }

    /**
     * change username of user
     * @param id
     * @param name
     */
    public void updateName(long id, String name) {
        UserInfoEntity user = userInfoRepository.findById(id).get();

        user.setUsername(name);
        userInfoRepository.save(user);

        for (Role r : roleRepository.findAll()) {
            r.setMembers(userInfoRepository.findAllByRolesContains(r));
            roleRepository.save(r);
        }
    }

    /**
     * change email address of a user
     * @param id
     * @param email
     */
    public void updateEmail(long id, String email) {
        UserInfoEntity user = userInfoRepository.findById(id).get();

        user.setEmail(email);
        userInfoRepository.save(user);

        for (Role r : roleRepository.findAll()) {
            r.setMembers(userInfoRepository.findAllByRolesContains(r));
            roleRepository.save(r);
        }
    }

    /**
     * get all online users
     * @return a list of users that are considered online
     */
    public List<UserDto> getAllOnlineUsers() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return getAll().stream().filter(x -> x.getLastOnline() != null).filter(x -> {
            try {
                return formatter.parse(x.getLastOnline()).getTime() > new Date(System.currentTimeMillis() - 1000 * 60 * 5).getTime(); // last online time has to be within the last 5 minutes
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
