package com.example.backend.ResponseDtos;

public class ApiKeyDto {
    String apiKey;

    public ApiKeyDto(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
