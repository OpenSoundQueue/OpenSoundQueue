package com.example.backend.rest;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.*;
import com.example.backend.annotations.AuthRequest;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.user_management.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SoundQueueRest {
    @Autowired
    private SongQueueService songQueueService;

    @Autowired
    private UserService userService;

    @GetMapping("/queue/all")
    public ResponseEntity<Object> getSongQueue() {
        return new ResponseEntity<>(new SongQueueDto(songQueueService.getQueue()), HttpStatus.OK);
    }

    @GetMapping("/queue/page/{page-number}")
    public ResponseEntity<Object> getPageWithDefaultSize(@PathVariable(name = "page-number") int pageNumber) {
        List<Song> pagedSongs = songQueueService.getQueue(pageNumber);
        if (songQueueService.getQueue().isEmpty())
            return new ResponseEntity<>(songQueueService.getQueue(), HttpStatus.OK);
        if (pagedSongs == null)
            return new ResponseEntity<>(new ErrorDto("page number does not exist"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new QueuePageDto(pageNumber, songQueueService.getTotalPages(), pagedSongs, songQueueService.getDefaultPageSize()), HttpStatus.OK);
    }

    @GetMapping("/queue/page/{page-number}/page-size/{page-size}")
    public ResponseEntity<Object> getPageWithCustomSize(@PathVariable(name = "page-number") int pageNumber, @PathVariable(name = "page-size") int pageSize) {
        List<Song> pagedSongs = songQueueService.getQueue(pageNumber, pageSize);
        if (songQueueService.getQueue().isEmpty())
            return new ResponseEntity<>(songQueueService.getQueue(), HttpStatus.OK);
        if (pagedSongs == null)
            return new ResponseEntity<>(new ErrorDto("page number does not exist"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new QueuePageDto(pageNumber, songQueueService.getTotalPages(pageSize), pagedSongs, pageSize), HttpStatus.OK);
    }

    @GetMapping("/queue/now-playing")
    public ResponseEntity<Object> getNowPlaying() {
        CurrentlyPlayingDto currentSong = songQueueService.getCurrentPlayingSong();
        if (currentSong == null) {
            return new ResponseEntity<>(new CurrentlyPlayingDto(false, 0, 0L, null), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(currentSong, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.ADD_SONG)
    @PostMapping("/queue/add")
    public ResponseEntity<Object> addSongToQueue(@RequestHeader(value = "X-API-KEY") String token, @RequestBody Map<String, String> requestBody) {
        Song song = songQueueService.addSong(requestBody.get("link"));
        userService.updateLastOnline(userService.getUserByToken(token));

        if (song != null) {
            return new ResponseEntity<>(song.getInfo(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorDto("Song could not be added"), HttpStatus.BAD_REQUEST);
        }
    }

    @AuthRequest(requiredPermission = Permissions.SKIP)
    @PostMapping("/queue/skip")
    public ResponseEntity<Object> skipSong(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));
        songQueueService.skip();
        return ResponseEntity.ok().build();
    }

    @AuthRequest(requiredPermission = Permissions.VOTESKIP)
    @GetMapping("/vote-skip/status")
    public ResponseEntity<Object> getVoteSkipStatus(@RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity user = userService.getUserByToken(token);
        userService.updateLastOnline(user);
        return new ResponseEntity<>(songQueueService.getVoteSkipStatus(user.getId()), HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.VOTESKIP)
    @GetMapping("/vote-skip/vote")
    public ResponseEntity<Object> setVoteSkip(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.setVoteSkip(token), HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.VOTESKIP)
    @GetMapping("/vote-skip/withdraw")
    public ResponseEntity<Object> withdrawVoteSkip(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.withdrawVoteSkip(token), HttpStatus.OK);
    }

    @GetMapping("/search/history/{search-term}/max-results/{max-results}")
    public ResponseEntity<Object> searchSongHistory(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "search-term") String searchTerm, @PathVariable(name = "max-results") int maxResults) {
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.searchSongHistory(searchTerm, maxResults), HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.PAUSE_PLAY)
    @PostMapping("/queue/start")
    public ResponseEntity<Object> startQueue(@RequestHeader(value = "X-API-KEY") String token) {
        if (!songQueueService.isPlaying()) songQueueService.play();
        userService.updateLastOnline(userService.getUserByToken(token));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @AuthRequest(requiredPermission = Permissions.PAUSE_PLAY)
    @PostMapping("/queue/stop")
    public ResponseEntity<Object> stopQueue(@RequestHeader(value = "X-API-KEY") String token) {
        if (songQueueService.isPlaying()) songQueueService.stop();
        userService.updateLastOnline(userService.getUserByToken(token));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @AuthRequest(requiredPermission = Permissions.CHANGE_ORDER)
    @PatchMapping("/queue/change-order")
    public ResponseEntity<Object> changeOrder(@RequestHeader(value = "X-API-KEY") String token, @RequestBody Map<String, Integer> positions) {
        int oldPos = positions.get("oldPos");
        int newPos = positions.get("newPos");

        if (oldPos >= songQueueService.getQueue().size() || oldPos < 0 || newPos < 0)
            return new ResponseEntity<>(new ErrorDto("Position of song does not exist"), HttpStatus.BAD_REQUEST);

        songQueueService.changeOrder(oldPos, newPos);
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(new SongQueueDto(songQueueService.getQueue()), HttpStatus.ACCEPTED);
    }

    @AuthRequest(requiredPermission = Permissions.REPLAY)
    @PostMapping("/queue/replay")
    public ResponseEntity<Object> replaySong(@RequestHeader(value = "X-API-KEY") String token) {
        if (songQueueService.getCurrentPlayingSong() == null)
            return new ResponseEntity<>(new ErrorDto("No song currently playing!"), HttpStatus.BAD_REQUEST);
        songQueueService.replaySong();
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @PostMapping("/queue/volume/{volume}")
    public ResponseEntity<Object> changeVolume(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "volume") int volume) {
        if (volume < 0 || volume > 100) {
            return new ResponseEntity<>(new ErrorDto("invalid value for 'volume'"), HttpStatus.BAD_REQUEST);
        }

        VolumeDto volumeDto = songQueueService.changeVolume(volume);

        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @GetMapping("/queue/current-volume")
    public ResponseEntity<Object> getVolume(@RequestHeader(value = "X-API-KEY") String token) {
        VolumeDto volumeDto = songQueueService.getVolume();

        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @PostMapping("/queue/mute")
    public ResponseEntity<Object> mute(@RequestHeader(value = "X-API-KEY") String token) {
        VolumeDto volumeDto = songQueueService.mute();
        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @PostMapping("/queue/unmute")
    public ResponseEntity<Object> unmute(@RequestHeader(value = "X-API-KEY") String token) {
        VolumeDto volumeDto = songQueueService.unmute();
        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }
}