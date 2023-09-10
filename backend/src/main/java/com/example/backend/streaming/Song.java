package com.example.backend.streaming;

public interface Song {
    SongInfo getInfo();
    int getCurrentTime();
    boolean isFetchingInfos();
}
