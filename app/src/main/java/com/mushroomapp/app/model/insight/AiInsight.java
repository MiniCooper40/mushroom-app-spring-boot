package com.mushroomapp.app.model.insight;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mushroomapp.app.controller.format.deserialize.AiInsightDeserializer;
import com.mushroomapp.app.model.storage.Media;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = AiInsightDeserializer.class)
public class AiInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_insight_id", nullable = false)
    private UUID id;

    @OneToOne(mappedBy = "aiInsight")
    private Media media;

    @OneToMany(mappedBy = "aiInsight")
    @ToString.Exclude
    @Builder.Default
    private List<MushroomClassificationSuggestion> suggestions = new LinkedList<>();

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Transactional
    public void addClassificationSuggestion(MushroomClassificationSuggestion suggestion) {
        this.suggestions.add(suggestion);
    }

}
