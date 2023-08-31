package com.example.backend.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.example.backend.ResponseDtos.QueuePageDto;

import java.io.IOException;
import java.util.Map;

public class QueuePageDtoSerializer extends JsonSerializer<QueuePageDto> {

    @Override
    public void serialize(QueuePageDto queuePageDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("numberOfPages", queuePageDto.getNumberOfPages());

        jsonGenerator.writeFieldName("page");
        jsonGenerator.writeStartArray();
        for (Object item : queuePageDto.getPage()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("numberInQueue", ((Map<String, Object>) item).get("numberInQueue"));

            // Serialize the "song" field using a custom serializer for Song class
            jsonGenerator.writeObjectField("song", ((Map<String, Object>) item).get("song"));

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}