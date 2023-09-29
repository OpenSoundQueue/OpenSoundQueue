package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserInfoRepository userInfoRepository;

    public UserInfoEntity getUserByUsername(String username) {
       List<UserInfoEntity> foundUsers = userInfoRepository.findByUsername(username);

       return foundUsers.get(0);
    }
}
