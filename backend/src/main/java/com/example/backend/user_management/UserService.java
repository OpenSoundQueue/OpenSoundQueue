package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import com.example.backend.ResponseDtos.UserDto;
import com.example.backend.util.TokenUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    public UserInfoEntity getUserByUsername(String username) {
       UserInfoEntity user = userInfoRepository.findByUsername(username);

       return user;
    }

    public UserInfoEntity getUserByToken(String token) {
        UserInfoEntity user = userInfoRepository.findByToken(tokenUtils.hashWithSHA512(token));

        return user;
    }

    public UserInfoEntity getUserById(Long id) {
        UserInfoEntity user = userInfoRepository.findById(id).orElse(null);

        return user;
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
}
