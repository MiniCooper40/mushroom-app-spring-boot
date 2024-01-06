package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mushroomapp.app.controller.format.response.CommentCreationResponse;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CommentCreateResponseSerializer extends JsonSerializer<CommentCreationResponse> {

    @Autowired
    private UserService userService;

    @Override
    public void serialize(CommentCreationResponse commentCreationResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        Optional<User> user = this.userService.currentUser();
        if(user.isEmpty()) throw new IOException("cannot find current user");

        jsonGenerator.writeStringField("profile_picture", "d");
    }
}
