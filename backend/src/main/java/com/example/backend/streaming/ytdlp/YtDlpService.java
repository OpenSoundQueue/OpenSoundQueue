/**
 * This service serves as endpoint between application and yt-dlp
 */

package com.example.backend.streaming.ytdlp;

import com.example.backend.Repository.SongInfoRepository;
import com.example.backend.streaming.ConvertSongTitle;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class YtDlpService {
    private static final Logger LOG = LoggerFactory.getLogger(YtDlpService.class);

    @Autowired
    SongInfoRepository songInfoRepository;

    @Autowired
    ConvertSongTitle convertSongTitle;

    /**
     * retrieve infos of a given song link
     * @param song song entity
     * @return infos of the given song
     */
    public SongInfo getInfos(Song song) {
        String title = "";
        String artist = "";
        String artistTag;
        String creatorTag;
        String channelTag;
        long duration = 0L;
        String artistFromTitle;

        try {
            Process process = Runtime.getRuntime().exec("yt-dlp --dump-single-json \"" + song.getLink() + "\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(line);
                title = (rootNode.get("title") + "").replaceAll("\"", "").replaceAll("(?i)\\[(official\\s*(?:video|music\\s*video)|visualiser|official\\s*audio)]|\\((official\\s*(?:video|music\\s*video)|visualiser|official\\s*audio)\\)", "");
                artist = "";
                artistTag = (rootNode.get("artist") + "").replaceAll("\"", "");
                creatorTag = (rootNode.get("creator") + "").replaceAll("\"", "");
                channelTag = (rootNode.get("channel") + "").replaceAll("\"", "");
                try {
                    duration = Math.round(Double.parseDouble(rootNode.get("duration") + ""));
                } catch (NumberFormatException e) {
                    throw new UnsupportedOperationException("\nURL:" + song.getLink() + "\nist kein Video, sondern ein Livestream/eine Premiere");
                }
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

    /**
     * sends a http request to a database of artists in order to verify whether the artist has been recognised correctly
     * @param artist name of the artist as string
     * @return boolean whether the artist is valid
     * @throws IOException
     * @throws InterruptedException
     */
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

    /**
     * downloads the audio file of a song in .wav format
     * @param song the song that is to be downloaded
     * @param downloadPath path where the audio file will be saved to
     * @param url link of the song that is to be downloaded
     */
    public void downloadSong(Song song, String downloadPath, String url) {
        Process p;
        try {
            String title = convertSongTitle.parseToFileName(song.getArtist() + " - " + song.getTitle());
            p = Runtime.getRuntime().exec("yt-dlp --output \"" + downloadPath + title + ".%(ext)s\" --extract-audio --audio-format wav \"" + url + "\"");
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
    }
}
