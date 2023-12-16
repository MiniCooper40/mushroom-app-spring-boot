package com.mushroomapp.app.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorage {

    void store(MultipartFile file);

}
