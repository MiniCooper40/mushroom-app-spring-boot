package com.mushroomapp.app.model.insight;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MushroomSimilarImage {

    @Id
    @Column(name = "similar_image_id")
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "license_name")
    private String licenseName;

    @Column(name = "license_url")
    private String licenseUrl;

    @Column(name = "citation")
    private String citation;

    @Column(name = "similarity")
    private float similarity;

    @Column(name = "url_small")
    private String urlSmall;

    @ManyToOne
    @JoinColumn(name = "classification_suggestion_id")
    private MushroomClassificationSuggestion classificationSuggestion;
}
