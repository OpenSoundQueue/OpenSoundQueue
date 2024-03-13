/**
 * this file contains everything responsible for controlling the song queue
 */

package com.example.backend.streaming;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.CurrentlyPlayingDto;
import com.example.backend.ResponseDtos.VolumeDto;
import com.example.backend.ResponseDtos.VoteSkipStatusDto;
import com.example.backend.user_management.UserService;
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

    /**
     * add a song to the queue
     * @param link of the song
     * @return infos of the song
     */
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

    /**
     * skips currently playing song
     */
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

    /**
     * changes order of a song in the queue
     * @param oldPos old position of a song
     * @param newPos new position of a song
     */
    public void changeOrder(int oldPos, int newPos) {
        Song sortedSong = songQueue.get(oldPos);

        songQueue.remove(oldPos);
        if (newPos >= songQueue.size()) {
            songQueue.add(sortedSong);
        } else {
            songQueue.add(newPos, sortedSong);
        }
    }

    /**
     * get all songs currently in the queue
     * @return list of songs
     */
    public List<Song> getQueue() {
        return this.songQueue;
    }

    /**
     * get all songs currently in the queue paged
     * @param pageNumber index of page
     * @return paged song queue
     */
    public List<Song> getQueue(int pageNumber) {
        if (getTotalPages() < pageNumber+1) return null;
        try {
            return this.songQueue.subList(pageNumber*defaultPageSize, pageNumber*defaultPageSize+defaultPageSize);
        } catch (IndexOutOfBoundsException e) {
            return this.songQueue.subList(pageNumber*defaultPageSize, this.songQueue.size());
        }
    }

    /**
     * get all songs currently in the queue paged with custom page size
     * @param pageNumber index of page
     * @param pageSize size of page
     * @return paged song queue
     */
    public List<Song> getQueue(int pageNumber, int pageSize) {
        if (getTotalPages(pageSize) < pageNumber+1) return null;
        try {
            return this.songQueue.subList(pageNumber*pageSize, pageNumber*pageSize+pageSize);
        } catch(IndexOutOfBoundsException e){
            return this.songQueue.subList(pageNumber*pageSize, this.songQueue.size());
        }
    }

    /**
     * start playback of current song
     */
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

    /**
     * stop playback of current song
     */
    public void stop() {
        songService.stop(currentSong);
        isPlaying = false;
    }

    /**
     * get amount of pages
     * @return integer amount of pages
     */
    public int getTotalPages() {
        return (int)Math.ceil((songQueue.size()*1.0)/(defaultPageSize*1.0));
    }

    /**
     * get amount of pages with custom page size
     * @param pageSize size of the pages
     * @return integer amount of pages
     */
    public int getTotalPages(int pageSize) {
        return (int)Math.ceil((songQueue.size()*1.0)/(pageSize*1.0));
    }

    /**
     * get default page size
     * @return integer
     */
    public int getDefaultPageSize() {
        return this.defaultPageSize;
    }

    /**
     * get current state of playback
     * @return boolean
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }

    /**
     * get currently playing song
     * @return current song
     */
    public CurrentlyPlayingDto getCurrentPlayingSong() {
        if (currentSong == null) return null;
        return new CurrentlyPlayingDto(isPlaying, currentSong.getCurrentTime(), System.currentTimeMillis(), currentSong.getInfo());
    }

    /**
     * get current status of the vote skip
     * @param userId id of the user that sent the request
     * @return current vote skip status
     */
    public VoteSkipStatusDto getVoteSkipStatus(Long userId) {
        boolean hasVoted = voteSkipUserList.contains(userId);
        return new VoteSkipStatusDto(hasVoted, voteSkipCurrent, getVoteSkipRequired());
    }

    /**
     * set a vote skip
     * @param userToken token of the user that sent the request
     * @return current vote skip status
     */
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

    /**
     * remove a vote skip
     * @param userToken token of the user that sent the request
     * @return current vote skip status
     */
    public VoteSkipStatusDto withdrawVoteSkip(String userToken) {
        UserInfoEntity user = userService.getUserByToken(userToken);
        if (!voteSkipUserList.contains(user.getId())) return getVoteSkipStatus(user.getId());
        voteSkipUserList.remove(user.getId());
        voteSkipCurrent--;
        return getVoteSkipStatus(user.getId());
    }

    /**
     * search song history for songs
     * @param searchTerm string
     * @param maxResults amount of songs that are returned
     * @return list of songs that match the search term
     */
    public List<SongInfoHistoryEntity> searchSongHistory(String searchTerm, int maxResults) {
        List<SongInfoHistoryEntity> ergs = songInfoRepository.findSongByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(searchTerm, searchTerm);
        if (ergs.size() > maxResults) {
            ergs = ergs.subList(0, maxResults);
        }
        return ergs;
    }

    /**
     * restart playback of current song
     */
    public void replaySong() {
        if (currentSong != null) {
            songService.replay(currentSong);
        }
    }

    /**
     * set playback volume
     * @param volume value between 0 and 100
     * @return state of playback volume
     */
    public VolumeDto changeVolume(int volume) {
        this.volume = volume;

        if (!this.isMuted && currentSong != null) {
            songService.changeVolume(currentSong, volume);
        }

        return new VolumeDto(this.volume, this.isMuted);
    }

    /**
     * get current state of playback volume
     * @return state of playback volume
     */
    public VolumeDto getVolume() {
        if (this.isMuted) {
            return new VolumeDto(this.volume, true);
        }

        return new VolumeDto(this.volume, false);
    }

    /**
     * mute playback
     * @return state of playback volume
     */
    public VolumeDto mute() {
        this.isMuted = true;

        if (currentSong != null) {
            songService.changeVolume(currentSong, 0);
        }

        return new VolumeDto(this.volume, this.isMuted);
    }

    /**
     * unmute playback
     * @return state of playback volume
     */
    public VolumeDto unmute() {
        this.isMuted = false;

        if (currentSong != null) {
            songService.changeVolume(currentSong, this.volume);
        }

        return new VolumeDto(this.volume, this.isMuted);
    }

    /**
     * get the amount of required vote skips to skip the current song
     * @return amount of required vote skips as integer
     */
    public int getVoteSkipRequired() {
        voteSkipRequired = (int) Math.ceil(userService.getAllOnlineUsers().size() / 2.0);
        return voteSkipRequired;
    }

    /**
     * reads a file of song links and loads them into the database
     * @param fileName filename of the file that contains song links
     */
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

    /**
     * removes a song from the queue
     * @param pos position of song
     * @param title of song
     * @return boolean whether it succeeded or not
     */
    public boolean removeSong(int pos, String title) {
        if (pos > songQueue.size()) return false;
        if (songQueue.get(pos).getTitle().trim().equals(title.trim())) {
            songQueue.remove(pos);
        } else {
            return false;
        }
        return true;
    }
}
