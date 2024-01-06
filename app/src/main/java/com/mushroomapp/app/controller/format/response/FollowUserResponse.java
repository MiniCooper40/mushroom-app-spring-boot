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
public class FollowUserResponse {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("followed_id")
    private UUID followedId;

}
