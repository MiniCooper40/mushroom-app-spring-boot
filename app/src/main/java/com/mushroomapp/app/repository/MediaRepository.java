package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.storage.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<Media, UUID> {

    Optional<Media> getMediaByFilename(String filename);
}
