package com.mushroomapp.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mushroomapp.app.controller.format.deserialize.AiInsightDeserializer;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.insight.AiInsight;
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

import java.io.File;
import java.io.IOException;
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

    @Autowired
    private AiInsightDeserializer deserializer;

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
        return directoryService.save("static/");
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

    private void loadAiInsight() throws IOException {
        File file = new File("C:\\Users\\ratbo\\Documents\\Code\\SpringBoot\\mushroom-v1\\app\\src\\test\\java\\com\\mushroomapp\\app\\deserialize\\insight1.txt");

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(AiInsight.class, deserializer);
        objectMapper.registerModule(module);

        AiInsight i = objectMapper.readValue(file, AiInsight.class);

        System.out.println("got ai insights: \n" + i);
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

        loadAiInsight();

        System.out.println("loaded ai insights");
    }
}
