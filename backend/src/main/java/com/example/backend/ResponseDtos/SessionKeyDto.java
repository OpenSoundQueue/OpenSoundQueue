package com.example.backend.ResponseDtos;

public class SessionKeyDto {
    String sessionKey;

    public SessionKeyDto(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
