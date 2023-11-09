package com.example.backend.Serializer;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.example.backend.ResponseDtos.QueuePageDto;
import com.example.backend.ResponseDtos.SongQueueDto;
import com.example.backend.streaming.Song;
import com.example.backend.streaming.SongInfo;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public SimpleModule customModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(QueuePageDto.class, new QueuePageDtoSerializer());
        module.addSerializer(Song.class, new SongSerializer());
        module.addSerializer(SongInfo.class, new SongInfoSerializer());
        module.addSerializer(SongInfoHistoryEntity.class, new SongHistoryInfoSerializer());
        module.addSerializer(SongQueueDto.class, new SongQueueDtoSerializer());
        return module;
    }
}
