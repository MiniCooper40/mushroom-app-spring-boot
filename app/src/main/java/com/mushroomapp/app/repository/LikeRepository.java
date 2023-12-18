package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.interaction.InteractionId;
import com.mushroomapp.app.model.interaction.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, InteractionId> {

    @Query("SELECT l FROM Like l WHERE l.id.userId = ?1 AND l.id.postId = ?2")
    Optional<Like> findLike(UUID userId, UUID postId);
}
