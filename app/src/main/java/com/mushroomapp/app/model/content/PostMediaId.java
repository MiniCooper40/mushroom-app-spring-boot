package com.mushroomapp.app.model.content;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;

import java.util.UUID;

@Embeddable
public class PostMediaId {

    @Column(name = "post_id")
    private UUID post;

    @Column(name = "media_id")
    private UUID media;
}
