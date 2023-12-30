package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.content.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findTop10ByOrderByTimestampDesc();
}
