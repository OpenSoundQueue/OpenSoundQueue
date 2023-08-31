package com.example.backend.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.example.backend.streaming.Song;

import java.io.IOException;

public class SongSerializer extends JsonSerializer<Song> {

    @Override
    public void serialize(Song song, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("info", song.getInfo()); // Customize this based on your Song class structure
        // Add more fields as needed
        jsonGenerator.writeEndObject();
    }
}