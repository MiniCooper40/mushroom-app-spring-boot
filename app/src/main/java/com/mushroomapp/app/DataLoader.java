package com.mushroomapp.app;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.service.DirectoryService;
import com.mushroomapp.app.service.MediaService;
import com.mushroomapp.app.service.PostService;
import com.mushroomapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private PostService postService;

    Directory d;

    List<String> filepaths = List.of(
            "mushroom1.jpg",
            "mushroom2.jpg",
            "mushroom3.jpg",
            "mushroom4.jpg",
            "mushroom5.png"
    );

    private User createUser(
            String username,
            String email,
            String token,
            String bio,
            Media profilePicture
    ) {
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setToken(token);
        u.setBio(bio);
        u.setProfilePicture(profilePicture);

        userService.save(u);

        return u;
    }

    private Media createProfilePicture(String file) {
        Media m = new Media();
        m.setFilename(file);
        m.setDirectory(d);

        mediaService.save(m);
        d.addMedia(m);

        return m;
    }

    private Directory createDirectory() {
        return directoryService.save("media/");
    }
    private List<Media> createPostMedia(List<String> files) {
//        Directory d = createDirectory();

        List<Media> media = new LinkedList<>();

        for(String file : files) {
            Media m = new Media();
            m.setFilename(file);
            m.setDirectory(d);
            d.addMedia(m);
            media.add(m);
        }

//        Media m1 = new Media();
//        m1.setFilename("dczANALPsbKetHQ.jpg");
//        m1.setDirectory(d);
//
//        Media m2 = new Media();
//        m2.setFilename("GLkPP6z4VSY5Cap.jpg");
//        m2.setDirectory(d);
//
//        d.addMedia(m1);
//        d.addMedia(m2);

        return media;
    }

    private Post createPost(User u, List<String> files) {
        List<Media> m = createPostMedia(files);

        return postService.createPost(
                u,
                m,
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
        );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        d = createDirectory();

        createProfilePicture("default-profile-picture.png");

        createPost(
                createUser(
                        "Test 123",
                        "mcdavidfan97@gmail.com",
                        "7mzLyTH5yUhxuVBQwjg8ASTIszs1",
                        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
                        createProfilePicture(filepaths.get(0))
                ),
                filepaths.subList(0,4)
        );

        createPost(
                createUser(
                        "Test ABC",
                        "testperson@gmail.com",
                        "2131hj2943u01294j",
                        "\"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.\"",
                        createProfilePicture(filepaths.get(4))
                ),
                filepaths.subList(2,4)
        );


        System.out.println("created post");
    }
}
