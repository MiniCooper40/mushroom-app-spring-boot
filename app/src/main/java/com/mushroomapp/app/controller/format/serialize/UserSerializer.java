package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserSerializer extends JsonSerializer<User> {

    @Autowired
    private UserService userService;

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        serializerProvider
                .defaultSerializeField("user", user, jsonGenerator);

        jsonGenerator
                .writeBooleanField(
                        "user_follows",
                        this.userService.currentUserFollows(user)
                        );
        jsonGenerator.writeEndObject();
    }
}
