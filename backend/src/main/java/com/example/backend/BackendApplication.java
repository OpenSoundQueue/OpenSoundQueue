package com.example.backend;

import com.example.backend.exceptions.UnsupportedSystemException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) throws UnsupportedSystemException {
        installYT_DLP();

        SpringApplication.run(BackendApplication.class, args);
    }

    private static void installYT_DLP() throws UnsupportedSystemException {
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
    }

    private static void ytdlpLinuxSetup() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("sudo apt install yt-dlp");
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            System.out.println("cmd output:");
            System.out.println("------------------------");
            while ((line = reader.readLine())!= null) {
                System.out.println(line);
            }
            System.out.println("------------------------");

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
        System.out.println("cmd output:");
        System.out.println("------------------------");
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) { break; }
            System.out.println(line);
        }
        System.out.println("------------------------");
    }

}
