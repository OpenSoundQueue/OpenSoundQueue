package com.example.backend.rest;

import com.example.backend.streaming.SongQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//TODO: remove these endpoint once functionality is implemented

@RestController
@RequestMapping(value = "/api")
public class TestEndpoint {
    @Autowired
    private SongQueueService songQueueService;

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
}
