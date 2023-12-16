package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.interaction.InteractionId;
import com.mushroomapp.app.model.interaction.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, InteractionId> {
}
