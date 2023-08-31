package com.example.backend.ResponseDtos;

import com.example.backend.streaming.Song;

import java.util.*;

public class QueuePageDto {
    int numberOfPages;
    List<Object> page;

    public QueuePageDto(int pageNumber, int numberOfPages, List<Song> songList, int pageSize) {
        this.numberOfPages = numberOfPages;
        this.page = new ArrayList<>();
        for (int i = pageNumber*pageSize; i < pageNumber*pageSize+songList.size(); i++) {
            Map<String, Object> temp = new TreeMap<>() {};
            temp.put("numberInQueue", i);
            temp.put("song", songList.get(i-pageNumber*pageSize).getInfo());
            page.add(temp);
        }
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public List<Object> getPage() {
        return page;
    }
}
