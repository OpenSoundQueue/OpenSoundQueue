package com.example.backend.streaming.youtube;

import com.example.backend.streaming.Song;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SongImplYoutube implements Song {
    private final String link;
    private boolean isDownloaded = false;
    private String fileName;
    private Clip clip = null;
    private boolean isStarted = false;

    public SongImplYoutube(String link) {
        this.link = link;
    }

    @Override
    public void play() {
        if (isStarted) {
            this.clip.start();
            return;
        }
        if (!isDownloaded) downloadSong();

        String basePath = System.getProperty("user.dir");
        String filePath = basePath + "\\" + fileName;
        System.out.println(filePath);
        File musicPath = new File(filePath);

        if (musicPath.exists()) {
            AudioInputStream audioInput = null;
            try {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
            try {
                this.clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            try {
                this.clip.open(audioInput);
            } catch (LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
            this.clip.start();
            System.out.println(this.clip.isRunning());
            this.isStarted = true;
        }
    }

    @Override
    public void stop() {
        this.clip.stop();
    }

    @Override
    public String getInfo() {
        return link;
    }

    public void downloadSong() {
        String url = this.link;

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "yt-dlp --extract-audio --audio-format wav \"" + url +"\"");
        builder.redirectErrorStream(true);
        Process p;
        try {
            p = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String fileName = "";
        System.out.println("cmd output:"); // TODO: beautify output
        System.out.println("------------------------");
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) { break; }
            System.out.println(line);
            if (line.contains("Destination: ")) {
                fileName = line.split("Destination: ")[1];
            }
        }
        System.out.println("------------------------");

        this.fileName = fileName;
        this.isDownloaded = true;
    }
}
