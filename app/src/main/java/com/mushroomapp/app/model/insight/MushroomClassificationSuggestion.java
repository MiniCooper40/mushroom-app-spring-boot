package com.mushroomapp.app.model.insight;

import com.mushroomapp.app.model.mushroom.Mushroom;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MushroomClassificationSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "classification_suggestion_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mushroom_id")
    private Mushroom mushroom;

    @ManyToOne
    @JoinColumn(name = "ai_insight_id")
    private AiInsight aiInsight;

    @Column(name = "probability")
    private float probability;

    @OneToMany(mappedBy = "classificationSuggestion")
    @Builder.Default
    private List<MushroomSimilarImage> similarImages = new LinkedList<>();

    @Transactional
    public void addSimilarImage(MushroomSimilarImage similarImage) {
        this.similarImages.add(similarImage);
    }

}
