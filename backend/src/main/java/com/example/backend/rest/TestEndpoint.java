package com.example.backend.rest;

import com.example.backend.streaming.SongQueueService;
import com.example.backend.streaming.youtube.SongImplYoutube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//TODO: remove these endpoint once functionality is implemented

@RestController
@RequestMapping(value = "/api")
public class TestEndpoint {
    @Autowired
    private SongQueueService songQueueService;

    @GetMapping("test")
    public String testEndpoint() {
        return "Success";
    }

    @GetMapping("skip")
    public String skip() {
        songQueueService.skip();
        return "skipped song!";
    }

    @GetMapping("play")
    public String play() {
        songQueueService.play();
        return "playing...";
    }

    @GetMapping("stop")
    public String stop() {
        songQueueService.stop();
        return "Stopped Song!";
    }

    @GetMapping("add")
    public String addSong(@PathVariable(required=false,name="link") String link) {
        link = "https://www.youtube.com/watch?v=tsmPCi7NKrg";
        songQueueService.addSong(new SongImplYoutube(link));
        return "added sound!";
    }

    @GetMapping("info")
    public String songInfo() {
        return songQueueService.getInfo().toString();
    }
}
