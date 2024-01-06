package com.mushroomapp.app.service;

import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.repository.MediaRepository;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media save(Media media) {
        return this.mediaRepository.save(media);
    }

    public Optional<Media> getMediaByFilename(String filename) {
        return this.mediaRepository.getMediaByFilename(filename);
    }

}
