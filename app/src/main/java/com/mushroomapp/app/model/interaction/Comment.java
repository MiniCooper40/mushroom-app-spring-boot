package com.mushroomapp.app.model.interaction;

import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.content.Post;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@IdClass(InteractionId.class)
public class Comment {

    @Id
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "post_id")
    private Post post;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}

