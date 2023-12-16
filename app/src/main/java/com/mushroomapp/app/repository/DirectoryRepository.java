package com.mushroomapp.app.repository;


import com.mushroomapp.app.model.storage.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, UUID> {
}
