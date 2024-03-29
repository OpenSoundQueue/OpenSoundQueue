/**
 * this service handles all actions with songs with YouTube as source
 */

package com.example.backend.streaming.youtube;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.streaming.*;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubeSongService implements SongServiceInterface {
    private static final Logger LOG = LoggerFactory.getLogger(SongImplYoutube.class);

    private List<Song> terminateSongs = new ArrayList<>();

    @Autowired
    SongQueueService songQueueService;

    @Autowired
    SongInfoRepository songInfoRepository;

    @Autowired
    YtDlpService ytDlpService;

    @Autowired
    ConvertSongTitle convertSongTitle;

    private int timeoutCounter = 0;

    /**
     * validates that a song link is correct
     * @param link of the song
     * @return song infos if the song is correct otherwise null
     */
    public Song validateSong(String link) {
        SongImplYoutube newSong = new SongImplYoutube(link);
        try {
            fetchInfos(newSong);
        } catch (UnsupportedOperationException e) {
            return null;
        }

        return newSong;
    }

    /**
     * start playback of the song
     * @param input the song that is to be started
     */
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
            return;
        }
        if (!song.isDownloaded()) this.downloadDependencies(song);

        String filePath = song.getDOWNLOAD_PATH() + convertSongTitle.parseToFileName(song.getFileName());
        File musicPath = new File(filePath);

        if (!musicPath.exists()) {
            if (timeoutCounter < 3) {
                LOG.error("Song could not be played as file was not found! File path: " + filePath);
                this.downloadDependencies(song);
                timeoutCounter++;
                play(song);
                return;
            }
            timeoutCounter = 0;
        }

        if (terminateSongs.contains(song)) return;

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
        if (songQueueService.getVolume().getIsMuted()) {
            changeVolume(song, 0);
        } else {
            changeVolume(song, songQueueService.getVolume().getVolume());
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
    }

    /**
     * stop playback of given song
     * @param input given song
     */
    @Override
    public void stop(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        song.getClip().stop();
    }

    /**
     * closes the audio stream of a given song
     * @param input given song
     */
    @Override
    public void close(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        if (song.getClip() != null) {
            song.getClip().close();
        } else {
            terminateSongs.add(song);
        }
    }

    /**
     * download audio file of the song
     * @param input song that is to be downloaded
     */
    @Override
    public void downloadDependencies(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        if (song.isDownloaded()) return;

        String url = song.getLink();

        ytDlpService.downloadSong(song, song.getDOWNLOAD_PATH(), url);

        song.setDownloaded(true);
    }

    /**
     * renew infos of a given song from yt-dlp
     * @param song given song
     */
    public void fetchInfos(Song song) {
        song.setFetchingInfos(true);
        List<SongInfoHistoryEntity> foundSongs = songInfoRepository.findBySongLink(song.getLink());
        if (foundSongs.isEmpty()) {
            SongInfo info = this.getInfos(song);
            song.setTitle(info.getTitle());
            song.setDuration(info.getDuration());
            song.setArtist(info.getArtist().trim());
            song.setFileName(song.getArtist() + " - " + song.getTitle() + ".wav");

            if (songInfoRepository.findByTitle(song.getTitle()).isEmpty()) {
                SongInfoHistoryEntity songInfoHistoryEntity = new SongInfoHistoryEntity(song.getTitle(), song.getArtist(), song.getLink(), song.getDuration());
                songInfoRepository.save(songInfoHistoryEntity);
            }
        } else {
            SongInfoHistoryEntity songHistory = foundSongs.get(0);
            song.setTitle(songHistory.getTitle());
            song.setDuration(songHistory.getDuration());
            song.setArtist(songHistory.getArtist().trim());
            song.setFileName(song.getArtist() + " - " + song.getTitle() + ".wav");
        }

        song.setFetchingInfos(false);
    }

    /**
     * get infos of a given song
     * @param input given song
     */
    @Override
    public SongInfo getInfos(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;

        if (song.getArtist() != null && song.getDuration() != null && song.getTitle() != null)
            return new SongInfo(song.getArtist(), song.getDuration(), song.getTitle());

        return ytDlpService.getInfos(input);
    }

    /**
     * restart playback of a given song
     * @param input given song
     */
    @Override
    public void replay(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        song.getClip().setMicrosecondPosition(0);
    }

    /**
     * change playback volume of a given song
     * @param input given song
     * @param volume integer between 0 and 100
     */
    @Override
    public void changeVolume(Song input, int volume) {
        SongImplYoutube song = (SongImplYoutube) input;
        Clip clip = song.getClip();
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10((float) volume / 100.0));
    }

    /**
     * get playback volume of a given song
     * @param input given song
     * @return playback volume as integer between 0 and 100
     */
    @Override
    public int getVolume(Song input) {
        SongImplYoutube song = (SongImplYoutube) input;
        Clip clip = song.getClip();
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (int) (Math.pow(10f, gainControl.getValue() / 20f)*100);
    }

}
