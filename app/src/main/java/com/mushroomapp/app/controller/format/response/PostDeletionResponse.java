package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDeletionResponse {

    @Builder.Default
    public LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("post_id")
    public UUID postId;
}