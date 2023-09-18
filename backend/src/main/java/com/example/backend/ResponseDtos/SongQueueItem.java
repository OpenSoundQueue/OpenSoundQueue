package com.example.backend.ResponseDtos;

import com.example.backend.streaming.SongInfo;

public class SongQueueItem {
    private int numberInQueue;
    private SongInfo song;

    public SongQueueItem(int numberInQueue, SongInfo song) {
        this.numberInQueue = numberInQueue;
        this.song = song;
    }

    public int getNumberInQueue() {
        return numberInQueue;
    }

    public SongInfo getSong() {
        return song;
    }
}
