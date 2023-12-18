package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public class CommentDeletionResponse {

    @Builder.Default
    public LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("comment_id")
    public UUID commentId;
}
