package com.mushroomapp.app.model.interaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.content.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
//@IdClass(InteractionId.class)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Like {

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

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
