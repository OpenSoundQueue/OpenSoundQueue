package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfoEntity getUserByUsername(String username) {
       List<UserInfoEntity> foundUsers = userInfoRepository.findByUsername(username);

       return foundUsers.get(0);
    }

    public UserInfoEntity registerNewUser(UserInfoEntity user) {
        String clearTextPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(clearTextPassword));

        return userInfoRepository.save(user);
    }
}
