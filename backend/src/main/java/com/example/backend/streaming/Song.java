package com.example.backend.streaming;

public interface Song {
    SongInfo getInfo();

    int getCurrentTime();

    boolean isFetchingInfos();

    void setFetchingInfos(boolean fetchingInfos);

    String getLink();

    String getArtist();

    String getTitle();

    Long getDuration();

    void setTitle(String title);

    void setDuration(Long duration);

    void setArtist(String artist);

    void setFileName(String fileName);
}
