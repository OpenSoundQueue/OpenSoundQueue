package com.example.backend;

import com.example.backend.exceptions.UnsupportedSystemException;
import com.example.backend.streaming.SongQueueService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class BackendApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    @Autowired
    private SongQueueService songQueueService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @PostConstruct
    @Order(1)
    private static void installYT_DLP() throws UnsupportedSystemException {
        LOG.info("Installing YT-DLP");
        Long timestamp = System.currentTimeMillis();

        String currentOs = System.getProperty("os.name");

        if (currentOs.contains("Windows")) {
            ytdlpWindowsSetup();
        } else if (currentOs.contains("Linux")) {
            throw new UnsupportedSystemException("Linux is currently not supported");
            //ytdlpLinuxSetup();
            // TODO: add linux support
        } else {
            throw new UnsupportedSystemException("Your current System is not supported");
        }

        LOG.info("Installed YT-DLP in " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    private static void ytdlpLinuxSetup() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("sudo apt install yt-dlp");
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                if (line.length() == 0 || line.charAt(0) == ' ') continue;
                LOG.info("YT-DLP: " + line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ytdlpWindowsSetup() {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "winget install yt-dlp");
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) { break; }
            if (line.length() == 0 || line.charAt(0) == ' ') continue;
            LOG.info("YT-DLP: " + line);
        }
    }

    @PostConstruct
    @Order(3)
    private void installFFMPEG() throws UnsupportedSystemException {
        LOG.info("Installing FFMPEG");
        Long timestamp = System.currentTimeMillis();

        String currentOs = System.getProperty("os.name");

        if (currentOs.contains("Windows")) {
            ffmpegWindowsSetup();
        } else if (currentOs.contains("Linux")) {
            throw new UnsupportedSystemException("Linux is currently not supported");
            //ffmpegLinuxSetup();
            // TODO: add linux support
        } else {
            throw new UnsupportedSystemException("Your current System is not supported");
        }

        LOG.info("Installed FFMPEG in " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    private void ffmpegWindowsSetup() {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "winget install ffmpeg");
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) { break; }
            if (line.length() == 0 || line.charAt(0) == ' ') continue;
            LOG.info("FFMPEG: " + line);
        }
    }

    @Profile("!prod")
    @Order(2)
    @PostConstruct
    private void feedTestData() {
        LOG.warn("Feeding song queue with test data... (might take a while)");
        for (int i = 0; i < 3; i++) {
            songQueueService.addSong("https://www.youtube.com/watch?v=_t431MAUQlQ");
            songQueueService.addSong("https://www.youtube.com/watch?v=dvgZkm1xWPE");
            songQueueService.addSong("https://www.youtube.com/watch?v=tsmPCi7NKrg");
            songQueueService.addSong("https://www.youtube.com/watch?v=OMOGaugKpzs");
            songQueueService.addSong("https://www.youtube.com/watch?v=MW_5jZ67z9E");
            songQueueService.addSong("https://www.youtube.com/watch?v=SlPhMPnQ58k");
            songQueueService.addSong("https://www.youtube.com/watch?v=VtGpN6dRADY");
            songQueueService.addSong("https://www.youtube.com/watch?v=yasj3j76SyM");
            songQueueService.addSong("https://www.youtube.com/watch?v=UnyLfqpyi94");
            songQueueService.addSong("https://www.youtube.com/watch?v=RBV3YFf76ow");
            songQueueService.addSong("https://www.youtube.com/watch?v=ds6o9in_y-o");
            songQueueService.addSong("https://www.youtube.com/watch?v=Y7ix6RITXM0");
            songQueueService.addSong("https://www.youtube.com/watch?v=pgN-vvVVxMA");
            songQueueService.addSong("https://www.youtube.com/watch?v=lxRwEPvL-mQ");
            songQueueService.addSong("https://www.youtube.com/watch?v=hTWKbfoikeg");
        }
    }

}
