package com.example.backend.Serializer;

import com.example.backend.Repository.SongInfoHistoryEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SongHistoryInfoSerializer extends JsonSerializer<SongInfoHistoryEntity> {

    @Override
    public void serialize(SongInfoHistoryEntity songInfoHistory, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("title", songInfoHistory.getTitle());
        jsonGenerator.writeStringField("artist", songInfoHistory.getArtist());
        jsonGenerator.writeNumberField("duration", songInfoHistory.getDuration());
        jsonGenerator.writeStringField("link", songInfoHistory.getSongLink());
        jsonGenerator.writeEndObject();
    }
}