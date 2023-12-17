package com.mushroomapp.app.model.interaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.content.Post;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
//@IdClass(InteractionId.class)
public class Comment {

    @EmbeddedId
    private InteractionId id = new InteractionId();

    //    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @JsonIgnore
    private User user;

    //    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    @MapsId("postId")
    @JsonIgnore
    private Post post;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}

