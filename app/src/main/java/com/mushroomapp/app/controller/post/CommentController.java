package com.mushroomapp.app.controller.post;

import com.mushroomapp.app.controller.FirebaseRequestReader;
import com.mushroomapp.app.controller.format.request.CommentCreationRequest;
import com.mushroomapp.app.controller.format.response.CommentCreationResponse;
import com.mushroomapp.app.controller.format.response.CommentDeletionResponse;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.interaction.Comment;
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
@RequestMapping("/v1/posts/comments")
public class CommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private InteractionService interactionService;

    private final FirebaseRequestReader firebaseRequestReader = new FirebaseRequestReader();

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentCreationRequest commentCreationRequest, HttpServletRequest request) {
        String content = commentCreationRequest.content;
        UUID postId = commentCreationRequest.postId;
        String userToken = firebaseRequestReader.getId(request);

        Optional<User> user = this.userService.getUserByToken(userToken);
        if(user.isEmpty()) throw new NoSuchElementException("could not find a user with token " + userToken);

        Optional<Post> post = this.postService.findPostById(postId);
        if(post.isEmpty()) throw new NoSuchElementException("could not find a post with id " + postId);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post.get());
        comment.setUser(user.get());

        Optional<Comment> commentRespondedTo = commentCreationRequest.respondedToId == null ?
                Optional.empty() :
                this.interactionService.findCommentById(
                        commentCreationRequest.respondedToId
                );

        if(commentRespondedTo.isPresent()) comment.setRespondedTo(commentRespondedTo.get());

        comment = this.interactionService.saveComment(comment);

        if(commentRespondedTo.isPresent()) commentRespondedTo.get().addResponse(comment);

        post.get().addComment(comment);
        user.get().addComment(comment);

        CommentCreationResponse response = CommentCreationResponse
                .builder()
                .commentId(comment.getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<Comment>> getAllCommentsOnPost(@PathVariable UUID id) {
        Optional<Post> post = this.postService.findPostById(id);

        if(post.isEmpty()) throw new NoSuchElementException("could not find post with id " + id);
        return ResponseEntity.ok(
                post.get().getComments()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id, HttpServletRequest request) {

        String userToken = firebaseRequestReader.getId(request);

        Optional<User> user = this.userService.getUserByToken(userToken);
        if(user.isEmpty()) throw new NoSuchElementException("could not find a user with token " + userToken);

        if(user.get().getComments().stream().noneMatch(it -> it.getId().equals(id))) throw new NoSuchElementException("user does not have a comment with id " + id);

        this.interactionService.deleteCommentById(id);

        CommentDeletionResponse response = CommentDeletionResponse
                .builder()
                .commentId(id)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Comment>> getUsersComments(HttpServletRequest request) {
        String userToken = firebaseRequestReader.getId(request);

        Optional<User> user = this.userService.getUserByToken(userToken);
        if(user.isEmpty()) throw new NoSuchElementException("could not find a user with token " + userToken);

        return ResponseEntity.ok(
                user.get().getComments()
        );
    }

}
