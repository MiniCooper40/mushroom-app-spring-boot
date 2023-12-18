package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("comment_id")
    public UUID commentId;

}
