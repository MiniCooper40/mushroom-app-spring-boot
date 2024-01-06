package com.mushroomapp.app.model.mushroom;

import com.mushroomapp.app.model.insight.MushroomClassificationSuggestion;
import jakarta.persistence.*;
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
public class Mushroom {

    @Id
    @Column(name = "mushroom_id")
    private String id;

    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "edibility")
    private String edibility;

    @OneToOne
    @JoinColumn(name = "description_id")
    private MushroomDescription description;

    @OneToMany(mappedBy = "mushroom")
    private List<MushroomClassificationSuggestion> suggestions;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "mushroom_common_names", joinColumns = @JoinColumn(name = "mushroom_id"))
    @Column(name = "common_names")
    private List<String> commonNames = new LinkedList<>();
}
