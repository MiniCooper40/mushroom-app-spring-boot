package com.mushroomapp.app.model.insight;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class InsightAIId {
    @Column(name = "media_id")
    private UUID media;

    @Column(name = "mushroom_id")
    private UUID mushroom;

    @Column(name = "user_id")
    private UUID user;
}
