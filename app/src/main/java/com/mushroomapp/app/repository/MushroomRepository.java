package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.mushroom.Mushroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MushroomRepository extends JpaRepository<Mushroom, String> {

}
