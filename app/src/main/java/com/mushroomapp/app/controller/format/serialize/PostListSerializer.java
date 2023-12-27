package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.storage.Media;

import java.io.IOException;
import java.util.List;

public class PostListSerializer extends JsonSerializer<List<Post>> {
    @Override
    public void serialize(List<Post> posts, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for(Post p : posts) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("post_id", p.getId().toString());
            jsonGenerator.writeStringField("user_id", p.getUser().getId().toString());
            jsonGenerator.writeNumberField("likes", p.getLikes().size());
            jsonGenerator.writeNumberField("comments", p.getComments().size());

            JsonSerializer<Media> mediaJsonSerializer = new SerializeMediaToPath();
            jsonGenerator.writeArrayFieldStart("media");
            for(PostMedia pm : p.getMedia()) {
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
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
