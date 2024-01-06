package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.mushroomapp.app.model.profile.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponse {

    @JsonUnwrapped
    private User user;

    @JsonProperty("user_follows")
    private boolean userFollows;
}
