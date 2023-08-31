package com.example.backend.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.example.backend.streaming.SongInfo;

import java.io.IOException;

public class SongInfoSerializer extends JsonSerializer<SongInfo> {

    @Override
    public void serialize(SongInfo songInfo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("artist", songInfo.getArtist());
        jsonGenerator.writeNumberField("duration", songInfo.getDuration());
        jsonGenerator.writeStringField("title", songInfo.getTitle());
        jsonGenerator.writeEndObject();
    }
}