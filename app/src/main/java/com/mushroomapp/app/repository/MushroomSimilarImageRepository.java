package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.insight.MushroomSimilarImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MushroomSimilarImageRepository extends JpaRepository<MushroomSimilarImage, UUID> {
}
