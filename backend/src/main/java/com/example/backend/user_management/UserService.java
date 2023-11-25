package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import com.example.backend.ResponseDtos.UserDto;
import com.example.backend.email.EmailComponent;
import com.example.backend.email.EmailUtils;
import com.example.backend.util.TokenUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {
    private UserInfoRepository userInfoRepository;
    private PasswordEncoder passwordEncoder;
    private TokenUtils tokenUtils;
    private EmailUtils emailUtils;
    private EmailComponent emailComponent;

    private Map<String, String> emailVerificationCodes = new HashMap<>();

    public UserService(
            UserInfoRepository userInfoRepository,
            PasswordEncoder passwordEncoder,
            TokenUtils tokenUtils,
            EmailUtils emailUtils,
            EmailComponent emailComponent
    ) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtils = tokenUtils;
        this.emailUtils = emailUtils;
        this.emailComponent = emailComponent;
    }

    public UserInfoEntity getUserByUsername(String username) {
        return userInfoRepository.findByUsername(username);
    }

    public UserInfoEntity getUserByToken(String token) {
        return userInfoRepository.findByToken(tokenUtils.hashWithSHA512(token));
    }

    public UserInfoEntity getUserById(Long id) {
        return userInfoRepository.findById(id).orElse(null);
    }

    public UserInfoEntity getUserByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    public boolean verifyApiKey(String token) {
        UserInfoEntity user = getUserByToken(token);

        if (user == null) {
            return false;
        }

        updateLastOnline(user);

        return true;
    }

    public UserInfoEntity registerNewAuthUser(UserInfoEntity user) {
        String clearTextPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(clearTextPassword));

        return userInfoRepository.save(user);
    }

    public UserInfoEntity registerNewUser(UserInfoEntity user) {
        return userInfoRepository.save(user);
    }

    public void updateToken(Long id, String token) {
        UserInfoEntity user = userInfoRepository.findById(id).orElse(null);

        if (user == null) {
            return;
        }

        user.setToken(tokenUtils.hashWithSHA512(token));

        userInfoRepository.save(user);
    }

    public void removeToken(UserInfoEntity user) {
        user.setToken(null);
        userInfoRepository.save(user);
    }

    public void updateLastOnline(UserInfoEntity user) {
        user.setLastOnline(new Date());
        userInfoRepository.save(user);
    }

    public List<UserDto> getAll() {
        List<UserDto> allUsers = userInfoRepository.findAll().stream().map(userInfoEntity -> new UserDto(userInfoEntity.getId(), userInfoEntity.getUsername(), userInfoEntity.getLastOnline())).toList();

        return allUsers;
    }

    public void deleteUser(Long id) {
        userInfoRepository.deleteById(id);
    }

    public void sendEmailVerification(UserInfoEntity user) throws MessagingException, IOException {
        String emailCode = emailUtils.generateEmailCode();
        emailVerificationCodes.put(user.getEmail(), emailCode);
        emailComponent.sendMail(user.getEmail(), emailCode, user.getUsername());
    }

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
}
