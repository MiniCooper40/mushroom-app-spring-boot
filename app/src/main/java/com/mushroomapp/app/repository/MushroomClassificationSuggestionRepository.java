package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.insight.MushroomClassificationSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MushroomClassificationSuggestionRepository extends JpaRepository<MushroomClassificationSuggestion, UUID> {
}
