package com.example.backend.streaming.youtube;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongInfo;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.streaming.SongServiceInterface;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class YoutubeSongService implements SongServiceInterface {
    private static final Logger LOG = LoggerFactory.getLogger(SongImplYoutube.class);

    @Autowired
    SongQueueService songQueueService;

    @Autowired
    SongInfoRepository songInfoRepository;

    public Song validateSong(String link) {
        SongImplYoutube newSong = new SongImplYoutube(link);
        try {
            initializeSong(newSong);
        } catch (UnsupportedOperationException e) {
            return null;
        }

        return newSong;
    }

    public void initializeSong(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;

        song.setFetchingInfos(true);
        List<SongInfoHistoryEntity> foundSongs = songInfoRepository.findBySongLink(song.getLink());
        if (foundSongs.size() == 0) {
            SongInfo info = this.getInfos(song);
            song.setTitle(info.getTitle().replaceAll("#?\\\\", "").replaceAll(":", " "));
            song.setDuration(info.getDuration());
            song.setArtist(info.getArtist());
            song.setFileName(song.getArtist() + " - " + song.getTitle() + ".wav");

            SongInfoHistoryEntity songInfoHistoryEntity = new SongInfoHistoryEntity(song.getTitle(), song.getArtist(), song.getLink(), song.getDuration());
            songInfoRepository.save(songInfoHistoryEntity);
        } else {
            SongInfoHistoryEntity songHistory = foundSongs.get(0);
            song.setTitle(songHistory.getTitle());
            song.setDuration(songHistory.getDuration());
            song.setArtist(songHistory.getArtist());
            song.setFileName(song.getArtist() + " - " + song.getTitle() + ".wav");
        }

        song.setFetchingInfos(false);
    }

    @Override
    public void play(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        while (song.isFetchingInfos()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (song.isStarted()) {
            song.getClip().start();
        }
        if (!song.isDownloaded()) this.downloadDependencies(song);

        String filePath = song.getDOWNLOAD_PATH() + song.getFileName();
        File musicPath = new File(filePath);

        if (musicPath.exists()) {
            AudioInputStream audioInput = null;
            try {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
            try {
                song.setClip(AudioSystem.getClip());
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            try {
                song.getClip().open(audioInput);
            } catch (LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
            song.getClip().start();
            try {
                audioInput.close();
                Files.deleteIfExists(Path.of(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            song.getClip().loop(0);
            song.setStarted(true);
            song.getClip().addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (song.getClip().getMicrosecondPosition() == song.getClip().getMicrosecondLength()) {
                        this.close(song);
                        songQueueService.skip();
                    }
                }
            });
        } else {
            LOG.error("Song could not be played as file was not found! File path: " + filePath);
            this.downloadDependencies(song);
            play(song);
        }
    }

    @Override
    public void stop(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        song.getClip().stop();
    }

    @Override
    public void close(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        song.getClip().close();
    }

    @Override
    public void downloadDependencies(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        if (song.isDownloaded()) return;

        String url = song.getLink();

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "yt-dlp --output \"" + song.getDOWNLOAD_PATH() + song.getArtist() + " - " + song.getTitle() + ".%(ext)s\" --extract-audio --audio-format wav \"" + url + "\"");
        builder.redirectErrorStream(true);
        Process p;
        try {
            p = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        LOG.info("Song download started for link: " + song.getLink());
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                break;
            }
        }
        LOG.info("Finished song download for link: " + song.getLink());

        song.setDownloaded(true);
    }

    @Override
    public SongInfo getInfos(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;

        if (song.getArtist() != null && song.getDuration() != null && song.getTitle() != null)
            return new SongInfo(song.getArtist(), song.getDuration(), song.getTitle());

        ProcessBuilder processBuilder = new ProcessBuilder().redirectErrorStream(false);

        String title = "";
        String artist = "";
        String artistTag;
        String creatorTag;
        String channelTag;
        long duration = 0L;
        String thumbnail = "";
        String artistFromTitle;

        processBuilder.command("cmd.exe", "/c", "yt-dlp --dump-single-json \"" + song.getLink() + "\"");
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
                    throw new UnsupportedOperationException("\nURL:" + song.getLink() + "\nist kein Video, sondern ein Livestream/eine Premiere");
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
}
