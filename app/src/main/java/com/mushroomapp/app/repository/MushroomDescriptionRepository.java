package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.mushroom.MushroomDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MushroomDescriptionRepository extends JpaRepository<MushroomDescription, UUID> {
}
