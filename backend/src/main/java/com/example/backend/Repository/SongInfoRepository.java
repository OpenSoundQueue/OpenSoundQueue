package com.example.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongInfoRepository extends JpaRepository<SongInfoHistoryEntity, Long> {
    List<SongInfoHistoryEntity> findBySongLink(String songLink);

    List<SongInfoHistoryEntity> findByTitle(String title);

    List<SongInfoHistoryEntity> findSongByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist);
}
