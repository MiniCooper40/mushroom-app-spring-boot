package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.insight.MushroomSimilarImage;
import com.mushroomapp.app.model.interaction.InteractionId;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, InteractionId> {

    @Query("SELECT l FROM Like l WHERE l.id.userId = ?1 AND l.id.postId = ?2")
    Optional<Like> findLike(UUID userId, UUID postId);


    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);
}
