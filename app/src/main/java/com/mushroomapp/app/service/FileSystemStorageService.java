package com.mushroomapp.app.service;

import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.model.storage.Media;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private MediaService mediaService;


    @Override
    public Media storeMultipartFileInDirectory(MultipartFile file, Directory directory) throws FileSystemException {
        String filename = RandomString.make(15) + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        System.out.println("Filename is " + filename);

        Media media = new Media();
        media.setDirectory(directory);
        media.setFilename(filename);

        directory.addMedia(media);

        try(InputStream is = file.getInputStream()) {
            Files.copy(is, Path.of(directory.getPath() + filename), StandardCopyOption.REPLACE_EXISTING);
            return this.mediaService.save(media);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileSystemException("Could not insert file");
        }
    }
}
