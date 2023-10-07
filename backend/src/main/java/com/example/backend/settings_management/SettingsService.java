package com.example.backend.settings_management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    /*@Value("${room.public}")
    private Boolean isPublic;

    @Value("$room.code}")
    private String roomCode;*/

    public void printSettings() {
        //System.out.println("is public: " + isPublic);
        //System.out.println("roomCode: " + roomCode);
    }
}
