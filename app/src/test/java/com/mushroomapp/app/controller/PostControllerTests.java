package com.mushroomapp.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.firebase.auth.FirebaseAuth;
import com.mushroomapp.app.controller.post.PostResponse;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTests {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseAuth firebaseAuth;

    private List<User> users;

    @Before
    public void reset() {
        users = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            User u = new User();
            u.setUsername("user" + i);
            u.setToken("token" + i);
            users.add(u);
        }

        for (User u : this.users) {
            userService.save(u);
            System.out.println(u);
        }
    }


    private MultipartBodyBuilder multipartBodyBuilder;

    @BeforeEach
    public void initializeFiles() {

        this.multipartBodyBuilder = new MultipartBodyBuilder();

        Resource image1 = new FileSystemResource("C:\\Users\\ratbo\\Pictures\\black-chicken-1.jpg");
        Resource image2 = new FileSystemResource("C:\\Users\\ratbo\\Pictures\\black-chicken-2.jpg");

        this.multipartBodyBuilder
                .part("file", image1);
        this.multipartBodyBuilder
                .part("file", image2);
        this.multipartBodyBuilder
                .part("caption", "I hope this works!!");
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    @Test
    public void postWith2ImagesThenDeletePost(@Autowired WebTestClient webClient) throws JsonProcessingException {

        String userToken = "QYFAZ0bIrAfMsxvD4oIXaTtXWS12";

        User user = new User();
        user.setToken(userToken);
        user.setUsername("test_user_123");

        user = webClient
                .post()
                .uri("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(writer.writeValueAsString(user)))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();

        assert user != null;

        System.out.println("User has id " + user.getId());

        PostResponse postResponse = webClient
                .post()
                .uri("/v1/posts/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", user.getToken())
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PostResponse.class)
                .returnResult()
                .getResponseBody();

        assert postResponse != null;

        System.out.println("Post has ID " + postResponse.id);

        List<Post> posts = webClient
                .get()
                .uri("v1/posts/user/"+user.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Post.class)
                .returnResult()
                .getResponseBody();

        assert posts != null;

        System.out.println("Found " + posts.size() + " posts");
        for(Post p : posts) {
            System.out.println("Post: " + p.getId());
        }

        assertThat(posts).map(Post::getUser).map(User::getId).containsOnly(user.getId());
        assertThat(posts).map(Post::getId).contains(postResponse.id);
    }

}
