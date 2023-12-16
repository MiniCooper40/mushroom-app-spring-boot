package com.mushroomapp.app.service;

import com.mushroomapp.app.repository.CommentRepository;
import com.mushroomapp.app.repository.LikeRepository;

public class InteractionService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public InteractionService(LikeRepository likeRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }
}
