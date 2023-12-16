package com.mushroomapp.app.service;

import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.model.storage.Media;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemException;

public interface StorageService {

    Media storeMultipartFileInDirectory(MultipartFile file, Directory directory) throws FileSystemException;
}
