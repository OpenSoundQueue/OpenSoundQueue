package com.example.backend.streaming;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.CurrentlyPlayingDto;
import com.example.backend.ResponseDtos.VolumeDto;
import com.example.backend.ResponseDtos.VoteSkipStatusDto;
import com.example.backend.user_management.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class SongQueueService {
    private static final Logger LOG = LoggerFactory.getLogger(SongQueueService.class);

    @Autowired
    SongService songService;

    @Autowired
    SongInfoRepository songInfoRepository;

    @Autowired
    UserService userService;

    @Value("${queue.page.size.default}")
    private int defaultPageSize;

    List<Song> songQueue = new LinkedList<>();
    Song currentSong = null;
    private boolean isPlaying = false;

    private int voteSkipRequired = 1;
    private int voteSkipCurrent = 0;

    private final Set<Long> voteSkipUserList = new HashSet<>();

    private int volume = 50;

    private boolean isMuted = false;

    public Song addSong(String link) {
        Song song = songService.validateSong(link);

        if (song == null) return null;

        if (currentSong == null) {
            currentSong = song;
            new Thread(this::play).start();
        } else {
            songQueue.add(song);
        }

        return song;
    }

    public void skip() {
        if (currentSong == null) return;
        songService.close(currentSong);
        voteSkipUserList.clear();
        if (songQueue.isEmpty()) {
            currentSong = null;
            isPlaying = false;
            return;
        }
        currentSong = songQueue.get(0);
        songQueue.remove(0);
        this.play();
    }

    public void changeOrder(int oldPos, int newPos) {
        Song sortedSong = songQueue.get(oldPos);

        songQueue.remove(oldPos);
        if (newPos >= songQueue.size()) {
            songQueue.add(sortedSong);
        } else {
            songQueue.add(newPos, sortedSong);
        }
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
        if (songQueue.isEmpty() && currentSong == null) {
            return;
        }
        songService.play(currentSong);

        if (!songQueue.isEmpty()) {
            new Thread(() -> songService.downloadDependencies(songQueue.get(0))).start();
        }

        isPlaying = true;
    }

    public void stop() {
        songService.stop(currentSong);
        isPlaying = false;
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
        if (currentSong == null) return null;
        return new CurrentlyPlayingDto(isPlaying, currentSong.getCurrentTime(), System.currentTimeMillis(), currentSong.getInfo());
    }

    public VoteSkipStatusDto getVoteSkipStatus(Long userId) {
        boolean hasVoted = voteSkipUserList.contains(userId);
        return new VoteSkipStatusDto(hasVoted, voteSkipCurrent, getVoteSkipRequired());
    }

    public VoteSkipStatusDto setVoteSkip(String userToken) {
        if (currentSong == null) {
            voteSkipUserList.clear();
            voteSkipCurrent = 0;
            return new VoteSkipStatusDto(false, 0, getVoteSkipRequired());
        }
        UserInfoEntity user = userService.getUserByToken(userToken);
        if (voteSkipUserList.contains(user.getId())) return getVoteSkipStatus(user.getId());
        if (voteSkipCurrent+1 >= getVoteSkipRequired()) {
            this.skip();
            voteSkipCurrent = 0;
        } else {
            voteSkipCurrent++;
            voteSkipUserList.add(user.getId());
        }

        return getVoteSkipStatus(user.getId());
    }

    public VoteSkipStatusDto withdrawVoteSkip(String userToken) {
        UserInfoEntity user = userService.getUserByToken(userToken);
        if (!voteSkipUserList.contains(user.getId())) return getVoteSkipStatus(user.getId());
        voteSkipUserList.remove(user.getId());
        voteSkipCurrent--;
        return getVoteSkipStatus(user.getId());
    }

    public List<SongInfoHistoryEntity> searchSongHistory(String searchTerm, int maxResults) {
        List<SongInfoHistoryEntity> ergs = songInfoRepository.findSongByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(searchTerm, searchTerm);
        if (ergs.size() > maxResults) {
            ergs = ergs.subList(0, maxResults);
        }
        return ergs;
    }

    public void replaySong() {
        if (currentSong != null) {
            songService.replay(currentSong);
        }
    }

    public VolumeDto changeVolume(int volume) {
        this.volume = volume;

        if (!this.isMuted && currentSong != null) {
            songService.changeVolume(currentSong, volume);
        }

        return new VolumeDto(this.volume, this.isMuted);
    }

    public VolumeDto getVolume() {
        if (this.isMuted) {
            return new VolumeDto(this.volume, true);
        }

        return new VolumeDto(this.volume, false);
    }

    public VolumeDto mute() {
        this.isMuted = true;

        if (currentSong != null) {
            songService.changeVolume(currentSong, 0);
        }

        return new VolumeDto(this.volume, this.isMuted);
    }

    public VolumeDto unmute() {
        this.isMuted = false;

        if (currentSong != null) {
            songService.changeVolume(currentSong, this.volume);
        }

        return new VolumeDto(this.volume, this.isMuted);
    }

    public int getVoteSkipRequired() {
        voteSkipRequired = (int) Math.ceil(userService.getAllOnlineUsers().size() / 2.0);
        return voteSkipRequired;
    }

    public void loadPreSetSongs(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        String resourcePath = "data/" + fileName;
        List<String> links = new LinkedList<>();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                links = reader.lines().toList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s:links) {
            new Thread(() -> songService.validateSong(s)).start();
        }
    }
}
