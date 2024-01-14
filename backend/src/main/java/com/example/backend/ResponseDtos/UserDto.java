package com.example.backend.ResponseDtos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDto {
    Long id;
    String username;
    Date lastOnline;

    public UserDto(Long id, String username, Date lastOnline) {
        this.username = username;
        this.id = id;
        this.lastOnline = lastOnline;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getLastOnline() {
        if (lastOnline == null) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return simpleDateFormat.format(lastOnline);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", lastOnline=" + lastOnline +
                '}';
    }
}
