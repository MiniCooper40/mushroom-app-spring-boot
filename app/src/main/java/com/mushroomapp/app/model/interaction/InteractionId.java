package com.mushroomapp.app.model.interaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InteractionId implements Serializable {

    @Serial
    private static final long serialVersionUID = -1231202940192840L;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private UUID userId;

    @Column(name = "post_id")
    @JsonProperty("post_id")
    private UUID postId;
}
