package com.example.backend.ResponseDtos;

public class VolumeDto {
    int volume;
    boolean isMuted;

    public VolumeDto(int volume, boolean isMuted) {
        this.volume = volume;
        this.isMuted = isMuted;
    }

    public int getVolume() {
        return volume;
    }

    public boolean getIsMuted() {
        return isMuted;
    }
}
