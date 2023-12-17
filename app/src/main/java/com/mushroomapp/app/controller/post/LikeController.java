package com.mushroomapp.app.controller.post;

import com.mushroomapp.app.controller.FirebaseRequestReader;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.InteractionService;
import com.mushroomapp.app.service.PostService;
import com.mushroomapp.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/posts")
public class LikeController {

    @Autowired
    private PostService postService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private UserService userService;

    private FirebaseRequestReader requestReader = new FirebaseRequestReader();

    @PostMapping("/like/{id}")
    public void likePost(@PathVariable UUID id, HttpServletRequest request) {
        String token = requestReader.getId(request);
        Optional<User> user = this.userService.getUserByToken(token);

        if(user.isEmpty()) throw new NoSuchElementException("user does not exist with token " + token);

        Optional<Post> post = this.postService.findPostById(id);
        if(post.isEmpty()) throw new NoSuchElementException("post does not exist with id " + id);

        this.interactionService.likePost(
                user.get(),
                post.get()
        );
    }
}
