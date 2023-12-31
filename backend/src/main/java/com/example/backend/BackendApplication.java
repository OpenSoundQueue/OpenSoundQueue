package com.example.backend;

import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.settings_management.PropertyService;
import com.example.backend.settings_management.SettingsService;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.user_management.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@PropertySource("classpath:system.properties")
public class BackendApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    @Autowired
    private SongQueueService songQueueService;

    @Autowired
    private UserService userService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private PropertyService propertyService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Profile("!prod")
    @Order(1)
    @PostConstruct
    private void feedTestData() {
        LOG.warn("Feeding song queue with test data... (might take a while)");
        for (int i = 0; i < 3; i++) {
            songQueueService.addSong("https://soundcloud.com/nfrealmusic/hope");
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

    @Profile("!prod")
    @Order(2)
    @PostConstruct
    private void feedTestUsers() throws IOException {
        LOG.info("Feeding test users");
        userService.registerNewAuthUser(new UserInfoEntity("Markus", "Passwort1!"));
        userService.registerNewAuthUser(new UserInfoEntity("Daniel", "Passwort2!"));
        userService.registerNewAuthUser(new UserInfoEntity("Luki", "Passwort3!"));
        userService.registerNewAuthUser(new UserInfoEntity("Eyüp", "Passwort4!"));
        userService.registerNewAuthUser(new UserInfoEntity("Toni", "Passwort5!"));
        userService.registerNewAuthUser(new UserInfoEntity("Nico Zach", "Passwort6!"));
        userService.registerNewAuthUser(new UserInfoEntity("Philip", "Passwort7!"));
        userService.registerNewAuthUser(new UserInfoEntity("Dren", "Passwort8!"));
        userService.registerNewAuthUser(new UserInfoEntity("Sebi", "Passwort9!"));
        userService.registerNewAuthUser(new UserInfoEntity("Markus II", "Passwort10!"));
        userService.registerNewAuthUser(new UserInfoEntity("Nils", "Passwort11!"));
        userService.registerNewAuthUser(new UserInfoEntity("Zyprian", "Passwort12!"));
        userService.registerNewAuthUser(new UserInfoEntity("Severin", "Passwort13!"));
        userService.registerNewAuthUser(new UserInfoEntity("Raphi", "Passwort14!"));
        userService.registerNewAuthUser(new UserInfoEntity("Luki Linke", "Passwort15!"));
        userService.registerNewAuthUser(new UserInfoEntity("Nina", "Passwort16!"));
        userService.registerNewAuthUser(new UserInfoEntity("Laurenz", "Passwort17!"));
        userService.registerNewAuthUser(new UserInfoEntity("Lorenz", "Passwort18!"));
        userService.registerNewAuthUser(new UserInfoEntity("Benji", "Passwort19!"));
        userService.registerNewAuthUser(new UserInfoEntity("Steffen Hofmann", "Passwort20!"));
        settingsService.printSettings();
        // propertyService.setProperty("room.public", "true"); // TODO: restart of application is required to apply changes
    }
}
