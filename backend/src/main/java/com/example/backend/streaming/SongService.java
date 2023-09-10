package com.example.backend.streaming;

import com.example.backend.streaming.youtube.YoutubeSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService implements SongServiceInterface {
    @Autowired
    YoutubeSongService youtubeSongService;

    @Override
    public void initializeSong(Song song) {
        getSongSource().initializeSong(song);
    }

    @Override
    public void play(Song song) {
        getSongSource().play(song);
    }

    @Override
    public void stop(Song song) {
        getSongSource().stop(song);
    }

    @Override
    public void close(Song song) {
        getSongSource().close(song);
    }

    @Override
    public void downloadDependencies(Song song) {
        getSongSource().downloadDependencies(song);
    }

    @Override
    public SongInfo getInfos(Song song) {
        return getSongSource().getInfos(song);
    }

    private SongServiceInterface getSongSource() {
        return youtubeSongService;
    }
}
