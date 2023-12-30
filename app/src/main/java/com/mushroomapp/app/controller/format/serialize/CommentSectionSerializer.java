package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mushroomapp.app.controller.format.response.CommentSection;
import com.mushroomapp.app.model.interaction.Comment;

import java.io.IOException;

public class CommentSectionSerializer extends JsonSerializer<CommentSection> {

    private final CommentSerializer commentSerializer = new CommentSerializer();

    @Override
    public void serialize(CommentSection commentSection, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeArrayFieldStart("comments");
        for(Comment c : commentSection.comments) {
            commentSerializer.serialize(
                    c,
                    jsonGenerator,
                    serializerProvider
            );
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
