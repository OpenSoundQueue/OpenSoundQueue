package com.example.backend.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class TokenUtils {

    @Bean
    public String generateToken() {
        String characterPool = "123467890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

        StringBuilder token = new StringBuilder();

        int tokenLength = 64;

        for (int i = 0; i < tokenLength; i++) {
            token.append(characterPool.charAt((int) (Math.random() * (characterPool.length()))));
        }

        return token.toString();
    }

    @Bean
    public String hashWithSHA512(String input) {
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
