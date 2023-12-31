package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import java.util.Optional;

@Component
public class PostSerializer extends JsonSerializer<Post> {

    @Autowired
    private UserService userService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private AwsService awsService;

    @Override
    public void serialize(Post post, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeArrayFieldStart("media");
        for(PostMedia pm : post.getMedia()) {
            jsonGenerator.writeStartObject();
            Media media = pm.getMedia();
            String filename = media.getFilename();
            String directoryPath = media.getDirectory().getPath();
            jsonGenerator.writeStringField("source", this.awsService.getSignedUrlForMedia(media));
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

        Optional<User> user = userService.currentUser();
        if(user.isEmpty()) jsonGenerator.writeBooleanField("user_likes", false);
        else jsonGenerator.writeBooleanField(
                "user_likes",
                this.interactionService.userLikesPost(
                        user.get(),
                        post
                )
        );

//        Media profilePicture = post.getUser().getProfilePicture();
//        String profilePicturePath = profilePicture.getDirectory().getPath() + profilePicture.getFilename();

        jsonGenerator.writeStringField("profile_picture", this.awsService.getSignedUrlForProfilePicture(post.getUser()));

        jsonGenerator.writeEndObject();
    }
}
