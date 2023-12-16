package com.mushroomapp.app.model.interaction;

import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.content.Post;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@IdClass(InteractionId.class)
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
