package com.example.backend.email;

import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    /**
     * this method is used to generate verification codes for emails
     * @return a verification code as string
     */
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
