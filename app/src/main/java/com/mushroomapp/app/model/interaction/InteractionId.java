package com.mushroomapp.app.model.interaction;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class InteractionId implements Serializable {
    @Column(name = "user_id")
    private UUID user;

    @Column(name = "post_id")
    private UUID post;
}
