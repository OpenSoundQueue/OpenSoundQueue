package com.example.backend.Repository;

import jakarta.persistence.*;

@Entity
@Table(name = "SongInfoHistory")
public class SongInfoHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "songLink")
    private String songLink;

    @Column(name = "duration")
    private Long duration;

    public SongInfoHistoryEntity() {

    }

    public SongInfoHistoryEntity(String title, String artist, String songLink, Long duration) {
        this.title = title;
        this.artist = artist;
        this.songLink = songLink;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "SongInfoHistoryEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", songLink='" + songLink + '\'' +
                ", duration=" + duration +
                '}';
    }
}