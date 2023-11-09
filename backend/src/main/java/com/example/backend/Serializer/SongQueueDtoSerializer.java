package com.example.backend.Serializer;

import com.example.backend.ResponseDtos.SongQueueDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class SongQueueDtoSerializer extends JsonSerializer<SongQueueDto> {

    @Override
    public void serialize(SongQueueDto songQueueDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Object item : songQueueDto.getSongqueue()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("numberInQueue", ((Map<String, Object>) item).get("numberInQueue"));

            // Serialize the "song" field using a custom serializer for Song class
            jsonGenerator.writeObjectField("song", ((Map<String, Object>) item).get("song"));

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}