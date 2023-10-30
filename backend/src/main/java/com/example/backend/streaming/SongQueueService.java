package com.example.backend.streaming;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.CurrentlyPlayingDto;
import com.example.backend.ResponseDtos.VoteSkipStatusDto;
import com.example.backend.user_management.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Value("${queue.voteskip.required}")
    private int voteSkipRequired;

    private int voteSkipCurrent = 0;

    private final List<UserInfoEntity> voteSkipUserList = new ArrayList<>();

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

    public void skip() { // TODO: fix race condition
        songService.close(currentSong);
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
        if (songQueue.size() == 0 && currentSong == null) {
            LOG.info("Song queue is empty!");
            return;
        }
        songService.play(currentSong);

        if (songQueue.size() > 0) {
            new Thread(() -> songService.downloadDependencies(songQueue.get(0))).start();
        }

        isPlaying = true;
    }

    public void stop() {
        songService.stop(currentSong);
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

    public VoteSkipStatusDto getVoteSkipStatus(String userToken) {
        boolean hasVoted = voteSkipUserList.contains(userService.getUserByToken(userToken));
        return new VoteSkipStatusDto(hasVoted, voteSkipCurrent, voteSkipRequired);
    }

    public VoteSkipStatusDto setVoteSkip(String userToken) {
        voteSkipUserList.add(userService.getUserByToken(userToken));
        if (voteSkipCurrent+1 >= voteSkipRequired) {
            this.skip();
            voteSkipCurrent = 0;
        } else {
            voteSkipCurrent++;
        }

        return getVoteSkipStatus(userToken);
    }

    public VoteSkipStatusDto withdrawVoteSkip(String userToken) {
        voteSkipUserList.remove(userService.getUserByToken(userToken));
        voteSkipCurrent--;
        return getVoteSkipStatus(userToken);
    }

    public List<SongInfoHistoryEntity> searchSongHistory(String searchTerm, int maxResults) {
        List<SongInfoHistoryEntity> ergs = songInfoRepository.findSongByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(searchTerm, searchTerm);
        if (ergs.size() > maxResults) {
            ergs = ergs.subList(0, maxResults);
        }
        return ergs;
    }
}
