package com.example.backend.streaming.soundcloud;

import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.File;

public class SongImplSoundcloud implements Song {
    private static final Logger LOG = LoggerFactory.getLogger(SongImplSoundcloud.class);
    private final String DOWNLOAD_PATH = System.getProperty("user.dir") + File.separator + ".song_downloads" + File.separator;

    private final String link;
    private boolean isDownloaded = false;
    private String fileName;
    private Clip clip = null;
    private boolean isStarted = false;

    private String title = null;
    private Long duration = null;
    private String artist = null;

    private boolean isFetchingInfos = true;

    public SongImplSoundcloud(String link) {
        this.link = link;
    }

    public SongInfo getInfo() {
        return new SongInfo(this.artist, this.duration, this.title);
    }

    public int getCurrentTime() {
        if (this.clip == null) {
            LOG.warn("Song has not been started yet and therefore cannot return the current time!");
            return 0;
        } else {
            return (int)this.clip.getMicrosecondPosition()/1_000;
        }
    }

    public boolean isFetchingInfos() {
        return this.isFetchingInfos;
    }

    public String getDOWNLOAD_PATH() {
        return DOWNLOAD_PATH;
    }

    public String getLink() {
        return link;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setFetchingInfos(boolean fetchingInfos) {
        isFetchingInfos = fetchingInfos;
    }
}
