/**
 * This service functions as a router between the song queue service and the individual song services
 * It decides which service to forward to based on the song link of the song provided
 */

package com.example.backend.streaming;

import com.example.backend.streaming.soundcloud.SoundcloudSongService;
import com.example.backend.streaming.youtube.YoutubeSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SongService implements SongServiceInterface {
    List<String> VALID_YOUTUBE_LINKS = Arrays.asList(
            ".*://(www\\.)?youtube.com/.*",
            ".*://(www\\.)?youtu.be/.*"
    );

    List<String> VALID_SOUNDCLOUD_LINKS = Arrays.asList(
            ".*://(www\\.)?soundcloud.com/.*",
            ".*://(on\\.)?soundcloud.com/.*"
    );

    Map<String, List<String>> LINK_VALIDATIONS = new HashMap<>() {{
        put("youtube", VALID_YOUTUBE_LINKS);
        put("soundcloud", VALID_SOUNDCLOUD_LINKS);
    }};

    @Autowired
    SoundcloudSongService soundcloudSongService;

    @Autowired
    YoutubeSongService youtubeSongService;

    @Override
    public Song validateSong(String link) {
        SongServiceInterface songService = getSongSource(link);
        if (songService == null) return null;
        return songService.validateSong(link);
    }

    @Override
    public void play(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return;
        songService.play(song);
    }

    @Override
    public void stop(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return;
        songService.stop(song);
    }

    @Override
    public void close(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return;
        songService.close(song);
    }

    @Override
    public void downloadDependencies(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return;
        songService.downloadDependencies(song);
    }

    @Override
    public SongInfo getInfos(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return null;
        return songService.getInfos(song);
    }

    public void replay(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return;
        songService.replay(song);
    }

    public void changeVolume(Song song, int volume) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return;
        songService.changeVolume(song, volume);
    }

    public int getVolume(Song song) {
        SongServiceInterface songService = getSongSource(song.getLink());
        if (songService == null) return -1;
        return songService.getVolume(song);
    }

    /**
     * Validation which type of source is being used
     */
    private SongServiceInterface getSongSource(String link) {
        if (!LINK_VALIDATIONS.get("youtube").stream().filter(link::matches).toList().isEmpty()) {
            return youtubeSongService;
        } else if (!LINK_VALIDATIONS.get("soundcloud").stream().filter(link::matches).toList().isEmpty()) {
            return soundcloudSongService;
        }

        return null;
    }
}
