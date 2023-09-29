package com.example.backend.ResponseDtos;

public class UserDto {
    String username;

    public UserDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
