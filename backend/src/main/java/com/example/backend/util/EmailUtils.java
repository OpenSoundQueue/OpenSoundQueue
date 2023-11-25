package com.example.backend.util;

import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
    public String generateEmailCode() {
        String characterPool = "1234567890";

        StringBuilder code = new StringBuilder();

        int codeLength = 6;

        for (int i = 0; i < codeLength; i++) {
            code.append(characterPool.charAt((int) (Math.random() * (characterPool.length()))));
        }

        return code.toString();
    }
}
