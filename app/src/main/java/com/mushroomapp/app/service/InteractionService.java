package com.mushroomapp.app.service;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.repository.CommentRepository;
import com.mushroomapp.app.repository.LikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InteractionService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public InteractionService(LikeRepository likeRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public void likePost(User user, Post post) {

        System.out.println("Trying to like post: " + post + "\n with user " + user);

        Like like = new Like();

        like.setPost(post);
        like.setUser(user);

        this.likeRepository.save(like);

        user.addLike(like);
        post.addLike(like);
    }

    public List<Like> findAllLikes() {
        return this.likeRepository.findAll();
    }

    public Comment saveComment(Comment comment) {
        return this.commentRepository.save(comment);
    }

    public void deleteCommentById(UUID commentId) {
        this.commentRepository.deleteById(commentId);
    }

    public Optional<Like> findLike(UUID userId, UUID postId) {
        return this.likeRepository.findLike(userId, postId);
    }

    public void deleteLike(Like like) {
        this.likeRepository.delete(like);
    }

    public Optional<Comment> findCommentById(UUID commentId) {
        return this.commentRepository.findById(commentId);
    }

    public boolean userLikesPost(User user, Post post) {
        return this.likeRepository.existsByUserAndPost(user, post);
    }

    @Transactional
    public void deleteLikeByUserAndPost(User user, Post post) {
        this.likeRepository.deleteByUserAndPost(user, post);
    }
}
