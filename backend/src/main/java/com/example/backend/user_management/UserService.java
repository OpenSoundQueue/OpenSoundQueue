package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import com.example.backend.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
}
