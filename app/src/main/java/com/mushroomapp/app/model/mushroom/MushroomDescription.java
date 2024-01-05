package com.mushroomapp.app.model.mushroom;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MushroomDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "description_id", nullable = false)
    private UUID id;

    @JoinColumn(unique = true, nullable = false)
    @OneToOne(mappedBy = "description")
    private Mushroom mushroom;

    @Lob
    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column(name = "citation")
    private String citation;

    @Column(name = "license_name")
    private String licenseName;

    @Column(name = "license_url")
    private String licenseUrl;
}
