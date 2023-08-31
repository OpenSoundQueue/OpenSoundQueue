package com.example.backend.streaming;

public class SongInfo {
    String artist;
    Long duration;
    String title;

    public SongInfo(String artist, Long duration, String title) {
        this.artist = artist;
        this.duration = duration;
        this.title = title;
    }

    @Override
    public String toString() {
        return "SongInfo{" +
                "artist='" + artist + '\'' +
                ", duration=" + duration +
                ", title='" + title + '\'' +
                '}';
    }

    public String getArtist() {
        return artist;
    }

    public Long getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }
}
