package com.example.backend.streaming.soundcloud;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongInfo;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.streaming.SongServiceInterface;
import com.example.backend.streaming.ytdlp.YtDlpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class SoundcloudSongService implements SongServiceInterface {
    private static final Logger LOG = LoggerFactory.getLogger(SongImplSoundcloud.class);

    @Autowired
    SongQueueService songQueueService;

    @Autowired
    SongInfoRepository songInfoRepository;

    @Autowired
    YtDlpService ytDlpService;

    public Song validateSong(String link) {
        SongImplSoundcloud newSong = new SongImplSoundcloud(link);
        try {
            fetchInfos(newSong);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return null;
        }

        return newSong;
    }

    @Override
    public void play(Song input) {
        SongImplSoundcloud song = (SongImplSoundcloud) input;
        while (song.isFetchingInfos()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (song.isStarted()) {
            song.getClip().start();
            return;
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
        SongImplSoundcloud song = (SongImplSoundcloud) input;
        song.getClip().stop();
    }

    @Override
    public void close(Song input) {
        SongImplSoundcloud song = (SongImplSoundcloud) input;
        song.getClip().close();
    }

    @Override
    public void downloadDependencies(Song input) {
        SongImplSoundcloud song = (SongImplSoundcloud) input;
        if (song.isDownloaded()) return;

        String url = song.getLink();

        ytDlpService.downloadSong(song, song.getDOWNLOAD_PATH(), url);

        song.setDownloaded(true);
    }

    public void fetchInfos(Song song) {
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
    public SongInfo getInfos(Song input) {
        SongImplSoundcloud song = (SongImplSoundcloud) input;

        if (song.getArtist() != null && song.getDuration() != null && song.getTitle() != null)
            return new SongInfo(song.getArtist(), song.getDuration(), song.getTitle());

        return ytDlpService.getInfos(input);
    }
}
