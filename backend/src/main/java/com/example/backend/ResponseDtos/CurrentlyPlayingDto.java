package com.example.backend.ResponseDtos;

import com.example.backend.streaming.SongInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrentlyPlayingDto {
    private boolean isPlaying;
    private int time;
    private Long stamp;
    private SongInfo song;

    public CurrentlyPlayingDto(boolean isPlaying, int time, Long stamp, SongInfo song) {
        this.isPlaying = isPlaying;
        this.time = time;
        this.stamp = stamp;
        this.song = song;
    }

    @JsonProperty(value="isPlaying")
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public SongInfo getSong() {
        return song;
    }

    public void setSong(SongInfo song) {
        this.song = song;
    }

    public int getTime() {
        return time;
    }

    public Long getStamp() {
        return stamp;
    }
}
