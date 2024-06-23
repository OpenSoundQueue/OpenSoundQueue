/**
 * all Rest endpoints related to the song queue
 */

package com.example.backend.rest;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.ResponseDtos.*;
import com.example.backend.annotations.AuthRequest;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.user_management.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SoundQueueRest {
    private SongQueueService songQueueService;
    private UserService userService;

    public SoundQueueRest(SongQueueService songQueueService, UserService userService) {
        this.songQueueService = songQueueService;
        this.userService = userService;
    }

    /**
     *
     * @return all songs in the queue
     */
    @GetMapping("/queue/all")
    public ResponseEntity<Object> getSongQueue() {
        return new ResponseEntity<>(new SongQueueDto(songQueueService.getQueue()), HttpStatus.OK);
    }

    /**
     * get songs in queue paged
     * @param pageNumber
     * @return songs of page in queue
     */
    @GetMapping("/queue/page/{page-number}")
    public ResponseEntity<Object> getPageWithDefaultSize(@PathVariable(name = "page-number") int pageNumber) {
        List<Song> pagedSongs = songQueueService.getQueue(pageNumber);
        if (songQueueService.getQueue().isEmpty())
            return new ResponseEntity<>(songQueueService.getQueue(), HttpStatus.OK);
        if (pagedSongs == null)
            return new ResponseEntity<>(new ErrorDto("page number does not exist"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new QueuePageDto(pageNumber, songQueueService.getTotalPages(), pagedSongs, songQueueService.getDefaultPageSize()), HttpStatus.OK);
    }

    /**
     * get songs in queue paged with custom page size
     * @param pageNumber
     * @param pageSize
     * @return songs of page in queue
     */
    @GetMapping("/queue/page/{page-number}/page-size/{page-size}")
    public ResponseEntity<Object> getPageWithCustomSize(@PathVariable(name = "page-number") int pageNumber, @PathVariable(name = "page-size") int pageSize) {
        List<Song> pagedSongs = songQueueService.getQueue(pageNumber, pageSize);
        if (songQueueService.getQueue().isEmpty())
            return new ResponseEntity<>(songQueueService.getQueue(), HttpStatus.OK);
        if (pagedSongs == null)
            return new ResponseEntity<>(new ErrorDto("page number does not exist"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new QueuePageDto(pageNumber, songQueueService.getTotalPages(pageSize), pagedSongs, pageSize), HttpStatus.OK);
    }

    /**
     *
     * @return currently playing song
     */
    @GetMapping("/queue/now-playing")
    public ResponseEntity<Object> getNowPlaying() {
        CurrentlyPlayingDto currentSong = songQueueService.getCurrentPlayingSong();
        if (currentSong == null) {
            return new ResponseEntity<>(new CurrentlyPlayingDto(false, 0, 0L, null), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(currentSong, HttpStatus.OK);
    }

    /**
     * add song to the queue
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param requestBody
     * @return status code
     */
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

    /**
     * skip currently playing song
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.SKIP)
    @PostMapping("/queue/skip")
    public ResponseEntity<Object> skipSong(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));
        songQueueService.skip();
        return ResponseEntity.ok().build();
    }

    /**
     * get status of current vote-skip
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status dto of vote-skip
     */
    @AuthRequest(requiredPermission = Permissions.VOTESKIP)
    @GetMapping("/vote-skip/status")
    public ResponseEntity<Object> getVoteSkipStatus(@RequestHeader(value = "X-API-KEY") String token) {
        UserInfoEntity user = userService.getUserByToken(token);
        userService.updateLastOnline(user);
        return new ResponseEntity<>(songQueueService.getVoteSkipStatus(user.getId()), HttpStatus.OK);
    }

    /**
     * add vote-skip
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.VOTESKIP)
    @GetMapping("/vote-skip/vote")
    public ResponseEntity<Object> setVoteSkip(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.setVoteSkip(token), HttpStatus.OK);
    }

    /**
     * withdraw vote-skip
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.VOTESKIP)
    @GetMapping("/vote-skip/withdraw")
    public ResponseEntity<Object> withdrawVoteSkip(@RequestHeader(value = "X-API-KEY") String token) {
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.withdrawVoteSkip(token), HttpStatus.OK);
    }

    /**
     * search songs in history
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param searchTerm
     * @param maxResults
     * @return all songs that match the search terms
     */
    @AuthRequest(requiredPermission = Permissions.HISTORY_SEARCH)
    @GetMapping("/search/history/{search-term}/max-results/{max-results}")
    public ResponseEntity<Object> searchSongHistory(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "search-term") String searchTerm, @PathVariable(name = "max-results") int maxResults) {
        if (maxResults < 0) return new ResponseEntity<>(new ErrorDto("invalid value for 'max-results'"), HttpStatus.BAD_REQUEST);
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(songQueueService.searchSongHistory(searchTerm, maxResults), HttpStatus.OK);
    }

    /**
     * start the playback of the currently playing song
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.PAUSE_PLAY)
    @PostMapping("/queue/start")
    public ResponseEntity<Object> startQueue(@RequestHeader(value = "X-API-KEY") String token) {
        if (!songQueueService.isPlaying()) songQueueService.play();
        userService.updateLastOnline(userService.getUserByToken(token));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * stop the playback of the currently playing song
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.PAUSE_PLAY)
    @PostMapping("/queue/stop")
    public ResponseEntity<Object> stopQueue(@RequestHeader(value = "X-API-KEY") String token) {
        if (songQueueService.isPlaying()) songQueueService.stop();
        userService.updateLastOnline(userService.getUserByToken(token));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * change order of the songs in the queue
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param positions
     * @return the newly sorted song queue
     */
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

    /**
     * start currently playing song from the beginning
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.REPLAY)
    @PostMapping("/queue/replay")
    public ResponseEntity<Object> replaySong(@RequestHeader(value = "X-API-KEY") String token) {
        if (songQueueService.getCurrentPlayingSong() == null)
            return new ResponseEntity<>(HttpStatus.OK);
        songQueueService.replaySong();
        userService.updateLastOnline(userService.getUserByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * change the volume of the playback
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param volume the delta for volume. >0: louder, <0: quieter
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @PostMapping("/queue/volume/{volume}")
    public ResponseEntity<Object> changeVolume(@RequestHeader(value = "X-API-KEY") String token, @PathVariable(name = "volume") int volume) {
        if (volume < -100 || volume > 100) {
            return new ResponseEntity<>(new ErrorDto("invalid value for 'volume'"), HttpStatus.BAD_REQUEST);
        }
        int currentVol = songQueueService.getVolume().getVolume();
        //Apply delta
        int newVol = currentVol + volume;
        //Clamp result to 0<= volume <= 100
        newVol = Math.max(0, Math.min(100, newVol));
        VolumeDto volumeDto = songQueueService.changeVolume(newVol);

        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    /**
     * get current volume of the playback
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @GetMapping("/queue/current-volume")
    public ResponseEntity<Object> getVolume(@RequestHeader(value = "X-API-KEY") String token) {
        VolumeDto volumeDto = songQueueService.getVolume();

        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    /**
     * mute playback
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @PostMapping("/queue/mute")
    public ResponseEntity<Object> mute(@RequestHeader(value = "X-API-KEY") String token) {
        VolumeDto volumeDto = songQueueService.mute();
        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    /**
     * unmute playback
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @return status code
     */
    @AuthRequest(requiredPermission = Permissions.CHANGE_VOLUME)
    @PostMapping("/queue/unmute")
    public ResponseEntity<Object> unmute(@RequestHeader(value = "X-API-KEY") String token) {
        VolumeDto volumeDto = songQueueService.unmute();
        return new ResponseEntity<>(volumeDto, HttpStatus.OK);
    }

    /**
     * delete a song from the queue
     * @param token is the access token of the user that sent the request. It is necessary if the @AuthRequest annotation is being used
     * @param input
     * @return
     */
    @AuthRequest(requiredPermission = Permissions.DELETE_SONGS)
    @DeleteMapping("/queue/delete")
    public ResponseEntity<Object> removeSongFromQueue(@RequestHeader(value = "X-API-KEY") String token, @RequestBody List<Map<String,String>> input) {
        input.sort((o1, o2) -> Integer.parseInt(o2.get("numberInQueue")) - Integer.parseInt(o1.get("numberInQueue")));
        for (Map<String, String> stringStringMap : input) {
            boolean erg = songQueueService.removeSong(Integer.parseInt(stringStringMap.get("numberInQueue")), stringStringMap.get("title"));
            if (!erg) return new ResponseEntity<>(new ErrorDto("Could not remove songs"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}