package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.storage.Media;

import java.io.IOException;

public class PostSerializer extends JsonSerializer<Post> {
    @Override
    public void serialize(Post post, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeArrayFieldStart("media");
        for(PostMedia pm : post.getMedia()) {
            jsonGenerator.writeStartObject();
            Media media = pm.getMedia();
            String filename = media.getFilename();
            String directoryPath = media.getDirectory().getPath();
            jsonGenerator.writeStringField("source", directoryPath+filename);
            jsonGenerator.writeNumberField("position", pm.getPosition());
            jsonGenerator.writeStringField("id", media.getId().toString());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeStringField("post_id", post.getId().toString());
        jsonGenerator.writeStringField("user_id", post.getUser().getId().toString());
        jsonGenerator.writeNumberField("comments", post.getComments().size());
        jsonGenerator.writeNumberField("likes", post.getLikes().size());
        jsonGenerator.writeStringField("username", post.getUser().getUsername());
        jsonGenerator.writeNumberField("followers", post.getUser().getFollowers().size());
        jsonGenerator.writeStringField("timestamp", post.getTimestamp().toString());
        jsonGenerator.writeStringField("caption", post.getCaption());

        Media profilePicture = post.getUser().getProfilePicture();
        String profilePicturePath = profilePicture.getDirectory().getPath() + profilePicture.getFilename();

        jsonGenerator.writeStringField("profile_picture", profilePicturePath);

        jsonGenerator.writeEndObject();
    }
}
