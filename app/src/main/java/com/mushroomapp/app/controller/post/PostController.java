package com.mushroomapp.app.controller.post;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.mushroomapp.app.aws.AwsService;
import com.mushroomapp.app.controller.FirebaseRequestReader;
import com.mushroomapp.app.controller.format.response.ExploreFeed;
import com.mushroomapp.app.controller.format.response.PostCreationResponse;
import com.mushroomapp.app.controller.format.response.PostDeletionResponse;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.service.*;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

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

    @Autowired
    private AwsService awsService;


    private Media store(MultipartFile file) throws IOException {

        String filename = RandomString.make(15) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Directory directory = directoryService.newestDirectory();

        System.out.println("Filename is " + filename);

        System.out.println("directory has path " + directory.getPath());

        Media media = new Media();
        media.setDirectory(directory);
        media.setFilename(filename);


        try(InputStream is = file.getInputStream()) {
//            Files.copy(is, Path.of("C:\\Users\\ratbo\\Documents\\Code\\SpringBoot\\mushroom-v1\\app\\src\\main\\resources\\static\\" + filename), StandardCopyOption.REPLACE_EXISTING);
            directory.addMedia(media);

            this.awsService.uploadFile(
                    file,
                    media.getFilename()
            );

//            String url = this.awsService.getSignedUrlForObjectKey(media.getFilename());
//            System.out.println("url to media = " + url);

            return this.mediaService.save(media);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileSystemException("Could not insert file");
        }
    }

    @PostMapping
    public ResponseEntity<PostCreationResponse> uploadPost(@RequestParam("files") MultipartFile[] files, @RequestParam("caption") String caption, HttpServletRequest request) throws IOException {
        System.out.println("In upload post. Caption is: " + caption);

        Optional<User> user = this.userService.currentUser();

        if(user.isEmpty()) throw new NoSuchElementException("could not identify current user");

        List<Media> mediaInPost = Arrays.stream(files).map(f -> {
            try {
                return store(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        Post posted = this.postService.createPost(user.get(), mediaInPost, caption);

        PostCreationResponse response = PostCreationResponse
                .builder()
                .postId(posted.getId())
                .build();

        return ResponseEntity.ok(response);
    }

//    @PostMapping
//    public ResponseEntity<PostCreationResponse> uploadPost(@RequestParam("file") MultipartFile[] files, @RequestParam("caption") String caption, HttpServletRequest request) throws IOException {
//        System.out.println("In upload post. Caption is: " + caption);
//
//        Optional<User> user = this.userService.currentUser();
//
//        if(user.isEmpty()) throw new NoSuchElementException("could not identify current user");
//
//        List<Media> mediaInPost = Arrays.stream(files).map(f -> {
//            try {
//                return store(f);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).toList();
//
//        Post posted = this.postService.createPost(user.get(), mediaInPost, caption);
//
//        PostCreationResponse response = PostCreationResponse
//                .builder()
//                .postId(posted.getId())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ExploreFeed> postsForUser(@PathVariable UUID id) throws BadRequestException {
        Optional<User> user = this.userService.getUserById(id);

        if(user.isEmpty()) throw new BadRequestException("User does not exist with ID: " + id);

        System.out.println("In PostController. Users posts are: " + user.get().getPosts());
        System.out.println("And user is " + user.get());

        return ResponseEntity.ok(
                new ExploreFeed(
                        user.get().getPosts()
                )
        );
    }

    @GetMapping("/user")
    public ResponseEntity<ExploreFeed> postsForCurrentUser() throws BadRequestException {
        Optional<User> user = this.userService.currentUser();

        if(user.isEmpty()) throw new BadRequestException("Could not determine current user");

        System.out.println("In PostController. Users posts are: " + user.get().getPosts());
        System.out.println("And user is " + user.get());

        return ResponseEntity.ok(
                new ExploreFeed(
                        user.get().getPosts()
                )
        );
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable UUID id) {
        Optional<Post> post = this.postService.findPostById(id);
        if(post.isPresent()) return post.get();
        throw new NoSuchElementException("no post exists with id " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostDeletionResponse> deletePost(@PathVariable UUID id) {
        this.postService.deletePostById(id);
        PostDeletionResponse response = PostDeletionResponse
                .builder()
                .postId(id)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/explore")
    public ResponseEntity<ExploreFeed> findMostRecent() {
        return ResponseEntity.ok(
                new ExploreFeed(
                        this.postService.findMostRecent()
                )
        );
    }
}
