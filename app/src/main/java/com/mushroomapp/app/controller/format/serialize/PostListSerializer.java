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

    @Override
    public void serialize(List<Post> posts, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        Optional<User> user = userService.currentUser();

        jsonGenerator.writeStartArray();
        for(Post p : posts) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("post_id", p.getId().toString());
            jsonGenerator.writeStringField("user_id", p.getUser().getId().toString());
            jsonGenerator.writeNumberField("likes", p.getLikes().size());
            jsonGenerator.writeNumberField("comments", p.getComments().size());
            jsonGenerator.writeStringField("caption", p.getCaption());
            jsonGenerator.writeStringField("timestamp", p.getTimestamp().toString());
            jsonGenerator.writeStringField("username", p.getUser().getUsername());


            if(user.isEmpty()) jsonGenerator.writeBooleanField("user_likes", false);
            else jsonGenerator.writeBooleanField(
                    "user_likes",
                    this.interactionService.userLikesPost(
                            user.get(),
                            p
                    )
            );

//            String profilePictureFilename = p.getUser().getProfilePicture().getFilename();
//            String profilePictureDirectoryPath = p.getUser().getProfilePicture().getDirectory().getPath();
            jsonGenerator.writeStringField("profile_picture", this.awsService.getSignedUrlForProfilePicture(p.getUser()));

            JsonSerializer<Media> mediaJsonSerializer = new SerializeMediaToPath();
            jsonGenerator.writeArrayFieldStart("media");
            for(PostMedia pm : p.getMedia()) {
                jsonGenerator.writeStartObject();
                Media media = pm.getMedia();
//                String filename = media.getFilename();
//                String directoryPath = media.getDirectory().getPath();
                jsonGenerator.writeStringField("source", this.awsService.getSignedUrlForMedia(media));
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
