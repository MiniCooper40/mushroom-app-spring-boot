package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreationResponse {

    @Builder.Default
    public LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("user_id")
    public UUID userId;
}
