package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.insight.AiInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiInsightRepository extends JpaRepository<AiInsight, UUID> {
}
