package com.mushroomapp.app.controller.format.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CommentCreationRequest {
    @JsonProperty("post_id")
    public UUID postId;

    @JsonProperty("responded_to_id")
    public UUID respondedToId = null;

    @JsonProperty("content")
    public String content;
}
