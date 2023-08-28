package com.example.backend.streaming;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SongQueue {
    List<Song> songQueue = new LinkedList<>();

    public void addSong(Song song) {
        songQueue.add(song);
    }

    public void skip() {
        this.stop();
        this.songQueue.remove(0);
        this.play();
    }

    public void changeOrder() {
        //TODO: implement
    }

    public List<Song> getQueue() {
        return this.songQueue;
    }

    public void play() {
        songQueue.get(0).play();
    }

    public void stop() {
        songQueue.get(0).stop();
    }

    public void getInfo() {
        songQueue.get(0).getInfo();
    }
}
