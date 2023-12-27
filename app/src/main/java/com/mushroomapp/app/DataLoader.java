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

    private User createUser() {
        d = createDirectory();
        User u = new User();
        u.setEmail("mcdavidfan97@gmail.com");
        u.setUsername("testUser");
        u.setToken("7mzLyTH5yUhxuVBQwjg8ASTIszs1");
        u.setBio("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.");
        u.setProfilePicture(
                createProfilePicture()
        );

        userService.save(u);

        return u;
    }

    private Media createProfilePicture() {
        Media m = new Media();
        m.setFilename("nCFYysKDX1hUfsp.jpg");
        m.setDirectory(d);

        mediaService.save(m);
        d.addMedia(m);

        return m;
    }

    private Directory createDirectory() {
        return directoryService.save("media/");
    }
    private List<Media> createPostMedia() {
//        Directory d = createDirectory();

        Media m1 = new Media();
        m1.setFilename("dczANALPsbKetHQ.jpg");
        m1.setDirectory(d);

        Media m2 = new Media();
        m2.setFilename("GLkPP6z4VSY5Cap.jpg");
        m2.setDirectory(d);

        d.addMedia(m1);
        d.addMedia(m2);

        return List.of(m1,m2);
    }

    private Post createPost() {
        User u = createUser();
        List<Media> m = createPostMedia();

        return postService.createPost(
                u,
                m,
                "This is a post!"
        );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createPost();
        System.out.println("created post");
    }
}
