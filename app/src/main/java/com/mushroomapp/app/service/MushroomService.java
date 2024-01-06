package com.mushroomapp.app.service;

import com.mushroomapp.app.model.mushroom.Mushroom;
import com.mushroomapp.app.model.mushroom.MushroomDescription;
import com.mushroomapp.app.repository.MushroomDescriptionRepository;
import com.mushroomapp.app.repository.MushroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MushroomService {

    @Autowired
    private MushroomRepository mushroomRepository;

    @Autowired
    private MushroomDescriptionRepository mushroomDescriptionRepository;

    public Mushroom save(Mushroom mushroom) {
        if(!this.existsById(mushroom.getId())) return this.mushroomRepository.save(mushroom);
        else return this.mushroomRepository.getReferenceById(mushroom.getId());
    }

    public boolean existsById(String id) {
        return this.mushroomRepository.existsById(id);
    }

    public Optional<Mushroom> getReferenceById(String id) {
        return this.mushroomRepository.findById(id);
    }

    public MushroomDescription save(MushroomDescription mushroomDescription) {
        return this.mushroomDescriptionRepository.save(mushroomDescription);
    }
}
