package com.example.backend.rest;

import com.example.backend.streaming.SongQueue;
import com.example.backend.streaming.youtube.SongImplYoutube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//TODO: remove these endpoint once functionality is implemented

@RestController
@RequestMapping(value = "/api")
public class TestEndpoint {
    @Autowired
    private SongQueue songQueue;

    @GetMapping("test")
    public String testEndpoint() {
        return "Success";
    }

    @GetMapping("play")
    public String play() {
        songQueue.play();
        return "playing...";
    }

    @GetMapping("stop")
    public String stop() {
        songQueue.stop();
        return "Stopped Song!";
    }

    @GetMapping("add")
    public String addSong(@PathVariable(required=false,name="link") String link) {
        link = "https://www.youtube.com/watch?v=tsmPCi7NKrg";
        System.out.println(link);
        songQueue.addSong(new SongImplYoutube(link));
        return "added sound!";
    }
}
