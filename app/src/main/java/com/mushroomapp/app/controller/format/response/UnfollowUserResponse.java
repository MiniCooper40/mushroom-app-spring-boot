package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UnfollowUserResponse {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("unfollowed_id")
    private UUID followedId;

}