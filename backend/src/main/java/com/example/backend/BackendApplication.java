package com.example.backend;

import com.example.backend.Repository.Role;
import com.example.backend.Repository.RoleRepository;
import com.example.backend.Repository.UserInfoRepository;
import com.example.backend.streaming.SongQueueService;
import com.example.backend.util.PropertyLoader;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BackendApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    private final SongQueueService songQueueService;
    private final RoleRepository roleRepository;
    private final UserInfoRepository userInfoRepository;
    private final PropertyLoader propertyLoader;

    public BackendApplication(SongQueueService songQueueService, RoleRepository roleRepository, UserInfoRepository userInfoRepository, PropertyLoader propertyLoader) {
        this.songQueueService = songQueueService;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
        this.propertyLoader = propertyLoader;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Order(1)
    @PostConstruct
    private void checkPreRequirements() throws IOException {
        File file = new File("./system.properties");
        if (!file.exists()) {
            Files.copy(propertyLoader.getPropertyStream(), Path.of("./system.properties"));
        }
    }

    @Order(2)
    @PostConstruct
    private void loadTestData() {
        //songQueueService.loadPreSetSongs("testSongs");
    }

    @Profile("!prod")
    @Order(2)
    //@PostConstruct
    private void feedTestData() {
        for (int i = 0; i < 3; i++) {
            LOG.warn("Feeding song queue with test data... (might take a while)");
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

    @PostConstruct
    public void fixUserRoles() {
        for (Role r : roleRepository.findAll()) {
            r.setMembers(userInfoRepository.findAllByRolesContains(r));
            roleRepository.save(r);
        }
    }
}
