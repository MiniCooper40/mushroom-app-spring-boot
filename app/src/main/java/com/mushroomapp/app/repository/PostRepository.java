package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.content.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
