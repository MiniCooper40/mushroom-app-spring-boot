package com.mushroomapp.app.service;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.repository.CommentRepository;
import com.mushroomapp.app.repository.LikeRepository;
import org.springframework.stereotype.Service;

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
}
