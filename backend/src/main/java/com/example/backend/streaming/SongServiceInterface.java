package com.example.backend.streaming;

public interface SongServiceInterface {
    Song validateSong(String link);
    void play(Song song);
    void stop(Song song);
    void close(Song song);
    void downloadDependencies(Song song);
    SongInfo getInfos(Song song);
    void replay(Song song);
}
