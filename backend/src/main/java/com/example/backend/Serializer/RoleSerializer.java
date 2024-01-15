package com.example.backend.Serializer;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.Role;
import com.example.backend.Repository.UserInfoEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RoleSerializer extends JsonSerializer<Role> {

    @Override
    public void serialize(Role role, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", role.getId());
        jsonGenerator.writeStringField("name", role.getName());
        jsonGenerator.writeArrayFieldStart("permissions");
        for (Permissions p : role.getPermissions()) {
            jsonGenerator.writeString(p.name());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeArrayFieldStart("members");
        if (role.getMembers() != null) {
            for (UserInfoEntity u : role.getMembers()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", u.getId());
                jsonGenerator.writeStringField("username", u.getUsername());
                jsonGenerator.writeEndObject();
            }
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}