package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mushroomapp.app.controller.format.serialize.CommentSerializer;
import com.mushroomapp.app.model.interaction.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreationResponse {

    @Builder.Default
    @JsonProperty("timestamp")
    public LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("comment")
    public Comment comment;
}
