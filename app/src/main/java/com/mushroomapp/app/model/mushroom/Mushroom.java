package com.mushroomapp.app.model.mushroom;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Mushroom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "mushroom_id")
    private UUID id;

    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "edibility")
    private String edibility;

    @Column(name = "description")
    private String description;
}
