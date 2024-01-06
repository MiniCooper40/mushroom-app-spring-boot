package com.mushroomapp.app.controller.post;

import com.mushroomapp.app.controller.FirebaseRequestReader;
import com.mushroomapp.app.controller.format.response.CommentDeletionResponse;
import com.mushroomapp.app.controller.format.response.LikeCreationResponse;
import com.mushroomapp.app.controller.format.response.LikeDeletionResponse;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.InteractionService;
import com.mushroomapp.app.service.PostService;
import com.mushroomapp.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/posts/likes")
public class LikeController {

    @Autowired
    private PostService postService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private UserService userService;

    private final FirebaseRequestReader requestReader = new FirebaseRequestReader();

    @PostMapping("/{id}")
    public ResponseEntity<LikeCreationResponse> likePost(@PathVariable UUID id, HttpServletRequest request) {

        Optional<User> user = this.userService.currentUser();
        System.out.println("in likePost w/" + user);

        if(user.isEmpty()) throw new NoSuchElementException("cannot identify requesting user");

        Optional<Post> post = this.postService.findPostById(id);
        if(post.isEmpty()) throw new NoSuchElementException("post does not exist with id " + id);

        if(this.interactionService.userLikesPost(
                user.get(),
                post.get()
        )) {
            System.out.println("deleted like");
            this.interactionService.deleteLikeByUserAndPost(
                    user.get(),
                    post.get()
            );
        }

        else {
            System.out.println("created like");
            this.interactionService.likePost(
                    user.get(),
                    post.get()
            );
        }

        LikeCreationResponse response = LikeCreationResponse
                .builder()
                .userId(user.get().getId())
                .postId(post.get().getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Object> findAllLikes() {
        return ResponseEntity
                .ok(
                        this.interactionService.findAllLikes()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LikeDeletionResponse> deleteLike(@PathVariable UUID postId, HttpServletRequest request) {
        String token = this.requestReader.getId(request);
        Optional<User> user = this.userService.getUserByToken(token);

        if(user.isEmpty()) throw new NoSuchElementException("could not find a user with token " + token);

        Optional<Post> post = this.postService.findPostById(postId);
        if(post.isEmpty()) throw new NoSuchElementException("could not find a post with id " + postId);

        Optional<Like> like = this.interactionService.findLike(
                user.get().getId(),
                post.get().getId()
        );

        if(like.isEmpty()) throw new NoSuchElementException("could not find a like with user_id " + user.get().getId() + ", and post_id " + post.get().getId());

        this.interactionService.deleteLike(
                like.get()
        );

        LikeDeletionResponse response = LikeDeletionResponse
                .builder()
                .userId(user.get().getId())
                .postId(post.get().getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<Like>> likesOnPost(@PathVariable UUID id) {
        Optional<Post> post = this.postService.findPostById(id);
        if(post.isEmpty()) throw new NoSuchElementException("could not find post with id " + id);

        return ResponseEntity.ok(post.get().getLikes());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Like>> usersLikes(HttpServletRequest request) {
        String token = this.requestReader.getId(request);
        Optional<User> user = this.userService.getUserByToken(token);

        if(user.isEmpty()) throw new NoSuchElementException("could not find a user with token " + token);

        return ResponseEntity.ok(
                user.get().getLikes()
        );
    }
}
