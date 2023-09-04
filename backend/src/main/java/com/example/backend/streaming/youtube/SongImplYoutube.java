package com.example.backend.streaming.youtube;

import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SongImplYoutube implements Song {
    private static final Logger LOG = LoggerFactory.getLogger(SongImplYoutube.class);
    private final String DOWNLOAD_PATH = System.getProperty("user.dir") + "\\.youtube_downloads\\";

    private final String link;
    private boolean isDownloaded = false;
    private String fileName;
    private Clip clip = null;
    private boolean isStarted = false;

    private String title = null;
    private Long duration = null;
    private String artist = null;

    public SongImplYoutube(String link) {
        this.link = link;

        new Thread(() -> {
            SongInfo info = getInfo();
            title = info.getTitle().replaceAll("#?\\\\", "");
            duration = info.getDuration();
            artist = info.getArtist();
            fileName = this.artist + " - " + this.title + ".wav";
        }).start();
    }

    @Override
    public Clip play() {
        if (isStarted) {
            this.clip.start();
            return this.clip;
        }
        if (!isDownloaded) downloadDependencies();

        String filePath = DOWNLOAD_PATH + fileName;
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
            try {
                audioInput.close();
                Files.deleteIfExists(Path.of(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.clip.loop(0);
            this.isStarted = true;
        } else {
            LOG.error("Song could not be played as file was not found! File path: " + filePath);
            downloadDependencies();
            return play();
        }

        return this.clip;
    }

    @Override
    public void stop() {
        this.clip.stop();
    }

    @Override
    public void close() {
        this.clip.close();
    }

    @Override
    public SongInfo getInfo() {
        if (this.artist != null && this.duration != null && this.title != null) return new SongInfo(this.artist, this.duration, this.title);

        ProcessBuilder processBuilder = new ProcessBuilder().redirectErrorStream(false);

        String title = "";
        String artist = "";
        String artistTag;
        String creatorTag;
        String channelTag;
        long duration = 0L;
        String thumbnail = "";
        String artistFromTitle;

        processBuilder.command("cmd.exe", "/c", "yt-dlp --dump-single-json \"" + this.link +"\"");
        StringBuilder stdout = new StringBuilder();
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                stdout.append(line).append("\n");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(line);
                title = (rootNode.get("title") + "").replaceAll("\"", "");
                artist = "";
                artistTag = (rootNode.get("artist") + "").replaceAll("\"", "");
                creatorTag = (rootNode.get("creator") + "").replaceAll("\"", "");
                channelTag = (rootNode.get("channel") + "").replaceAll("\"", "");
                try {
                    duration = Long.parseLong(rootNode.get("duration") + "");
                } catch (NumberFormatException e) {
                    throw new UnsupportedOperationException("\nURL:" + this.link + "\nist kein Video, sondern ein Livestream/eine Premiere");
                }
                thumbnail = (rootNode.get("thumbnails").get(0).get("url") + "").replaceAll("\"", "");
                artistFromTitle = "null";

                if (title.contains(" - ")) {
                    if (checkDatabaseForArtist(title.split(" - ")[0])) {
                        artistFromTitle = title.split(" - ")[0];
                    } else if (checkDatabaseForArtist(title.split(" - ")[1])) {
                        artistFromTitle = title.split(" - ")[1];
                    }
                }

                if (!artistTag.equals("null")) {
                    artist = artistTag;
                } else if (!artistFromTitle.equals("null")) {
                    artist = artistFromTitle;
                    title = title.split(" - ")[1];
                } else if (!creatorTag.equals("null")) {
                    artist = creatorTag;
                } else {
                    artist = channelTag;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(new SongInfo(artist, duration, title));

        return new SongInfo(artist, duration, title);
    }

    public void downloadDependencies() {
        if (this.isDownloaded) return;

        String url = this.link;

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "yt-dlp --output \""+ this.DOWNLOAD_PATH + this.artist + " - " + this.title + ".%(ext)s\" --extract-audio --audio-format wav \"" + url +"\"");
        builder.redirectErrorStream(true);
        Process p;
        try {
            p = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        LOG.info("Song download started for link: " + this.link);
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) { break; }
        }
        LOG.info("Finished song download for link: " + this.link);

        this.isDownloaded = true;
    }

    private boolean checkDatabaseForArtist(String artist) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://musicbrainz.org/search?query=" + URLEncoder.encode(artist, StandardCharsets.UTF_8) + "&type=artist&limit=25&method=indexed"))
                .build();
        StringBuilder answer = new StringBuilder(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        long counter = 0;
        while (answer.indexOf(artist) >= 0) {
            counter++;
            answer.replace(answer.indexOf(artist), answer.indexOf(artist) + artist.length(), "");
        }
        return counter > 2;
    }

    @Override
    public int getCurrentTime() {
        return (int)this.clip.getMicrosecondPosition()/1_000_000;
    }
}
