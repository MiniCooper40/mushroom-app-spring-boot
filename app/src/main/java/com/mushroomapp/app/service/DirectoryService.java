package com.mushroomapp.app.service;

import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.repository.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    public Directory save(String pathname) {
        Directory directory = new Directory();
        directory.setPath(pathname);
        return this.directoryRepository.save(directory);
    }

    public Directory newestDirectory() {
        return this.directoryRepository.findAll().get(0);
    }

}
