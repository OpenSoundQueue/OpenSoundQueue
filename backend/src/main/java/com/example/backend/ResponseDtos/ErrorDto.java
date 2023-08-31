package com.example.backend.ResponseDtos;

public class ErrorDto {
    String error;

    public ErrorDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
