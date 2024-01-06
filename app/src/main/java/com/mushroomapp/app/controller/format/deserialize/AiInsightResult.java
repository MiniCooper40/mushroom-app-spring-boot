package com.mushroomapp.app.controller.format.deserialize;

import com.mushroomapp.app.model.insight.MushroomClassificationSuggestion;
import com.mushroomapp.app.model.insight.MushroomSimilarImage;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AiInsightResult {
    private MushroomClassificationSuggestion mushroomClassificationSuggestion;
    private List<MushroomSimilarImage> mushroomSimilarImageList;
}
