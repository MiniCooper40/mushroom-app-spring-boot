package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeCreationResponse {

    @Builder.Default
    public LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("post_id")
    public UUID postId;

    @JsonProperty("user_id")
    public UUID userId;
}
