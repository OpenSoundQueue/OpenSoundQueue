package com.example.backend.rest;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.*;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.user_management.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SoundQueueRest {
    @Autowired
    private SongQueueService songQueueService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/queue/all")
    public ResponseEntity<Object> getSongQueue() {
        List<Song> songList = songQueueService.getQueue();
        List<SongQueueItem> responseBody = new ArrayList<>();

        for (int i = 0; i < songList.size(); i++) {
            Song s = songList.get(i);
            responseBody.add(new SongQueueItem(i, s.getInfo()));
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/queue/page/{page-number}")
    public ResponseEntity<Object> getPageWithDefaultSize(@PathVariable(required = true, name = "page-number") int pageNumber) {
        List<Song> pagedSongs = songQueueService.getQueue(pageNumber);
        if (pagedSongs == null)
            return new ResponseEntity<>(new ErrorDto("page number does not exist"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new QueuePageDto(pageNumber, songQueueService.getTotalPages(), pagedSongs, songQueueService.getDefaultPageSize()), HttpStatus.OK);
    }

    @GetMapping("/queue/page/{page-number}/page-size/{page-size}")
    public ResponseEntity<Object> getPageWithCustomSize(@PathVariable(required = true, name = "page-number") int pageNumber, @PathVariable(required = true, name = "page-size") int pageSize) {
        List<Song> pagedSongs = songQueueService.getQueue(pageNumber, pageSize);
        if (pagedSongs == null)
            return new ResponseEntity<>(new ErrorDto("page number does not exist"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new QueuePageDto(pageNumber, songQueueService.getTotalPages(pageSize), pagedSongs, pageSize), HttpStatus.OK);
    }

    @GetMapping("/queue/now-playing")
    public ResponseEntity<Object> getNowPlaying() {
        if (songQueueService.getQueue().size() == 0)
            return new ResponseEntity<>(new ErrorDto("No song currently playing"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(songQueueService.getCurrentPlayingSong(), HttpStatus.OK);
    }

    @PostMapping("/queue/add")
    public ResponseEntity<Object> addSongToQueue(@RequestBody Map<String, String> requestBody) {
        Song song = songQueueService.addSong(requestBody.get("link"));

        if (song != null) {
            return new ResponseEntity<>(song.getInfo(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorDto("Song could not be added"), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/vote-skip/status")
    public ResponseEntity<Object> getVoteSkipStatus() {
        return new ResponseEntity<>(songQueueService.getVoteSkipStatus(), HttpStatus.OK);
    }

    @GetMapping("/vote-skip/vote")
    public ResponseEntity<Object> setVoteSkip() {
        return new ResponseEntity<>(songQueueService.setVoteSkip(), HttpStatus.OK);
    }

    @GetMapping("/vote-skip/withdraw")
    public ResponseEntity<Object> withdrawVoteSkip() {
        return new ResponseEntity<>(songQueueService.withdrawVoteSkip(), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        UserInfoEntity userInfoEntity = userService.getUserByUsername(username);

        if (userInfoEntity == null) {
            return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(password, userInfoEntity.getPassword())) {
            String token = userService.generateToken();

            userService.updateToken(userInfoEntity.getId(), token);
            return new ResponseEntity<>(new ApiKeyDto(token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ErrorDto("Incorrect username or password"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verify(@RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity userInfoEntity = userService.getUserByToken(token);

        if (userInfoEntity == null) {
            return new ResponseEntity<>(new ErrorDto("Invalid API key"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new UserDto(userInfoEntity.getUsername()), HttpStatus.OK);
    }
}
