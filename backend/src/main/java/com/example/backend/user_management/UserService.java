package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfoEntity getUserByUsername(String username) {
       UserInfoEntity user = userInfoRepository.findByUsername(username);

       return user;
    }

    public UserInfoEntity registerNewUser(UserInfoEntity user) {
        String clearTextPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(clearTextPassword));

        return userInfoRepository.save(user);
    }

    public void updateToken(Long id, String token) {
        UserInfoEntity user = userInfoRepository.findById(id).orElse(null);

        if (user == null) {
            return;
        }

        user.setToken(token);

        userInfoRepository.save(user);
    }
}
