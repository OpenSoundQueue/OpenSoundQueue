package com.example.backend.streaming;

import com.example.backend.ResponseDtos.CurrentlyPlayingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.util.LinkedList;
import java.util.List;

@Service
public class SongQueueService {
    private static final Logger LOG = LoggerFactory.getLogger(SongQueueService.class);

    @Value("${queue.page.size.default}")
    private int defaultPageSize;

    List<Song> songQueue = new LinkedList<>();
    Song currentSong = null;
    private boolean isPlaying = false;

    public void addSong(Song song) {
        if (currentSong == null) {
            currentSong = song;
            new Thread(this::play).start();
        } else {
            songQueue.add(song);
        }
    }

    public void skip() {
        this.currentSong.close();
        currentSong = songQueue.get(0);
        songQueue.remove(0);
        this.play();
    }

    public void changeOrder() {
        //TODO: implement
    }

    public List<Song> getQueue() {
        return this.songQueue;
    }

    public List<Song> getQueue(int pageNumber) {
        if (getTotalPages() < pageNumber+1) return null;
        try {
            return this.songQueue.subList(pageNumber*defaultPageSize, pageNumber*defaultPageSize+defaultPageSize);
        } catch (IndexOutOfBoundsException e) {
            return this.songQueue.subList(pageNumber*defaultPageSize, this.songQueue.size());
        }
    }

    public List<Song> getQueue(int pageNumber, int pageSize) {
        if (getTotalPages(pageSize) < pageNumber+1) return null;
        try {
            return this.songQueue.subList(pageNumber*pageSize, pageNumber*pageSize+pageSize);
        } catch(IndexOutOfBoundsException e){
            return this.songQueue.subList(pageNumber*pageSize, this.songQueue.size());
        }
    }

    public void play() {
        if (songQueue.size() == 0) {
            LOG.info("Song queue is empty!");
            return;
        }
        Object songStream = currentSong.play();

        if (songQueue.size() > 0) {
            new Thread(() -> songQueue.get(0).downloadDependencies()).start();
        }

        isPlaying = true;

        // handle what happens after song is finished and next should start
        if (songStream instanceof Clip songStreamClip) {
            songStreamClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (songStreamClip.getMicrosecondPosition() == songStreamClip.getMicrosecondLength()) {
                        songStreamClip.close();
                        skip();
                    }
                }
            });
        } else {
            LOG.error("Unknown song stream type!");
        }
    }

    public void stop() {
        currentSong.stop();
        isPlaying = false;
    }

    public SongInfo getInfo() {
        return currentSong.getInfo();
    }

    public int getTotalPages() {
        return (int)Math.ceil((songQueue.size()*1.0)/(defaultPageSize*1.0));
    }

    public int getTotalPages(int pageSize) {
        return (int)Math.ceil((songQueue.size()*1.0)/(pageSize*1.0));
    }

    public int getDefaultPageSize() {
        return this.defaultPageSize;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public CurrentlyPlayingDto getCurrentPlayingSong() {
        return new CurrentlyPlayingDto(isPlaying, currentSong.getCurrentTime(), System.currentTimeMillis(), currentSong.getInfo());
    }
}