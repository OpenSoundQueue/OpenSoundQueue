package com.example.backend.ResponseDtos;

public class PrivacySettingsDto {
    boolean isPrivate;
    String entryCode;

    public PrivacySettingsDto(boolean isPrivate, String entryCode) {
        this.isPrivate = isPrivate;
        this.entryCode = entryCode;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
}
