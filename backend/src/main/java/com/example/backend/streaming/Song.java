package com.example.backend.streaming;

public interface Song {
    Object play();
    void stop();
    void close();
    void downloadDependencies();
    SongInfo getInfo();
    int getCurrentTime();
    boolean isFetchingInfos();
}
