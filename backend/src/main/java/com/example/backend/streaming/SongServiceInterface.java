package com.example.backend.streaming;

public interface SongServiceInterface {
    void initializeSong(Song song);
    void play(Song song);
    void stop(Song song);
    void close(Song song);
    void downloadDependencies(Song song);
    SongInfo getInfos(Song song);
}
