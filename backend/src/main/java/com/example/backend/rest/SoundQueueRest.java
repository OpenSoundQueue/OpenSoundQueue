package com.example.backend.rest;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.*;
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
            return new ResponseEntity<>(new StatusDto("No song currently playing"), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(currentSong, HttpStatus.OK);
    }

    @PostMapping("/queue/add")
    public ResponseEntity<Object> addSongToQueue(@RequestHeader(value = "X-API-KEY") String token, @RequestBody Map<String, String> requestBody) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        Song song = songQueueService.addSong(requestBody.get("link"));
        userService.updateLastOnline(userService.getUserByToken(token));

        if (song != null) {
            return new ResponseEntity<>(song.getInfo(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorDto("Song could not be added"), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/queue/skip")
    public ResponseEntity<Object> skipSong(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }
        userService.updateLastOnline(userService.getUserByToken(token));
        songQueueService.skip();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vote-skip/status")
    public ResponseEntity<Object> getVoteSkipStatus(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }
        UserInfoEntity user = userService.getUserByToken(token);
        userService.updateLastOnline(user);
        return new ResponseEntity<>(songQueueService.getVoteSkipStatus(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/vote-skip/vote")
    public ResponseEntity<Object> setVoteSkip(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.setVoteSkip(token), HttpStatus.OK);
    }

    @GetMapping("/vote-skip/withdraw")
    public ResponseEntity<Object> withdrawVoteSkip(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.withdrawVoteSkip(token), HttpStatus.OK);
    }

    @GetMapping("/search/history/{search-term}/max-results/{max-results}")
    public ResponseEntity<Object> searchSongHistory(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "search-term") String searchTerm, @PathVariable(name = "max-results") int maxResults) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.searchSongHistory(searchTerm, maxResults), HttpStatus.OK);
    }

    @PostMapping("/queue/start")
    public ResponseEntity<Object> startQueue(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        if (!songQueueService.isPlaying()) songQueueService.play();
        userService.updateLastOnline(userService.getUserByToken(token));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/queue/stop")
    public ResponseEntity<Object> stopQueue(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        if (songQueueService.isPlaying()) songQueueService.stop();
        userService.updateLastOnline(userService.getUserByToken(token));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/queue/change-order")
    public ResponseEntity<Object> changeOrder(@RequestHeader(value = "X-API-KEY") String token, @RequestBody Map<String, Integer> positions) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        int oldPos = positions.get("oldPos");
        int newPos = positions.get("newPos");

        if (oldPos >= songQueueService.getQueue().size() || oldPos < 0 || newPos < 0) return new ResponseEntity<>(new ErrorDto("Position of song does not exist"), HttpStatus.BAD_REQUEST);

        songQueueService.changeOrder(oldPos, newPos);
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(new SongQueueDto(songQueueService.getQueue()), HttpStatus.ACCEPTED);
    }

    @PostMapping("/queue/replay")
    public ResponseEntity<Object> replaySong(@RequestHeader(value = "X-API-KEY") String token) {
        if (!userService.verifyApiKey(token)) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.UNAUTHORIZED);
        }

        songQueueService.replaySong();
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
