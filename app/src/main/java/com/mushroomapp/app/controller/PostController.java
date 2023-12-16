package com.mushroomapp.app.controller;

import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.service.*;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/posts")
public class PostController {

    private final StorageService storageService = new FileSystemStorageService();
    private final FirebaseRequestReader firebaseRequestReader = new FirebaseRequestReader();

    @Autowired
    private PostService postService;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;


    private Media store(MultipartFile file) throws IOException {

        String filename = RandomString.make(15) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Directory directory = directoryService.newestDirectory();

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

        //return this.storageService.storeMultipartFileInDirectory(file, directoryService.newestDirectory());
    }

    @PostMapping("/")
    public ResponseEntity<Object> uploadPost(@RequestParam("file") MultipartFile[] files, @RequestParam("caption") String caption, HttpServletRequest request) throws IOException {
        System.out.println("In upload post. Caption is: " + caption);

        String token = this.firebaseRequestReader.getId(request);
        User user = this.userService.getUserByToken(token);

        List<Media> mediaInPost = Arrays.stream(files).map(f -> {
            try {
                return store(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        this.postService.createPost(user, mediaInPost, caption);

        return ResponseEntity.ok().build();
    }
}
