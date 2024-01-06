package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mushroomapp.app.aws.AwsService;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.service.InteractionService;
import com.mushroomapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class PostListSerializer extends JsonSerializer<List<Post>> {

    @Autowired
    private UserService userService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private AwsService awsService;

    @Autowired
    private PostSerializer postSerializer;

    @Override
    public void serialize(List<Post> posts, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();
        for(Post post : posts) {
            this.postSerializer.serialize(
                    post,
                    jsonGenerator,
                    serializerProvider
            );
        }
        jsonGenerator.writeEndArray();
    }
}
