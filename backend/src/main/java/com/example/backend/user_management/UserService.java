package com.example.backend.user_management;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public UserInfoEntity getUserByToken(String token) {
        UserInfoEntity user = userInfoRepository.findByToken(hashWithSHA512(token));

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

        user.setToken(hashWithSHA512(token));

        userInfoRepository.save(user);
    }

    public String generateToken() {
        String characterPool = "123467890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

        StringBuilder token = new StringBuilder();

        int tokenLength = 64;

        for (int i = 0; i < tokenLength; i++) {
            token.append(characterPool.charAt((int) (Math.random() * (characterPool.length()))));
        }

        return token.toString();
    }

    private static String hashWithSHA512(String input) {
        try {
            MessageDigest sha512Digest = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = sha512Digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
