package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.interaction.InteractionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
