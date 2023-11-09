package com.example.backend.ResponseDtos;

import com.example.backend.streaming.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SongQueueDto {
    List<Object> songqueue;

    public SongQueueDto(List<Song> songList) {
        this.songqueue = new ArrayList<>();
        for (int i = 0; i < songList.size(); i++) {
            Map<String, Object> temp = new TreeMap<>() {};
            temp.put("numberInQueue", i);
            temp.put("song", songList.get(i).getInfo());
            songqueue.add(temp);
        }
    }

    public List<Object> getSongqueue() {
        return songqueue;
    }
}
