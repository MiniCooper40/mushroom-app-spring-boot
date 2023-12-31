package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mushroomapp.app.aws.AwsService;
import com.mushroomapp.app.model.interaction.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommentSerializer extends JsonSerializer<Comment> {

    @Autowired
    private AwsService awsService;

    @Override
    public void serialize(Comment comment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        System.out.println("In comment serializer w/ comment " + comment);

        try {
            jsonGenerator.writeStringField("content", comment.getContent());
            jsonGenerator.writeStringField("timestamp", comment.getTimestamp().toString());
            jsonGenerator.writeStringField("username", comment.getUser().getUsername());
            jsonGenerator.writeStringField("profile_picture", this.awsService.getSignedUrlForCommentProfilePicture(comment));
            jsonGenerator.writeStringField("comment_id", comment.getId().toString());
            jsonGenerator.writeStringField("user_id", comment.getUser().getId().toString());

            Comment respondedTo = comment.getRespondedTo();
            if(respondedTo == null) jsonGenerator.writeStringField("replied_to", "none");
            else jsonGenerator.writeStringField("replied_to", respondedTo.getId().toString());

            jsonGenerator.writeNumberField("number_of_responses", comment.getResponses().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("done comment serializer");
        jsonGenerator.writeEndObject();
    }
}
