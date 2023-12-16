package com.mushroomapp.app.model.content;

import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user.user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new LinkedList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new LinkedList<>();

    @OneToMany(mappedBy = "post")
    private List<PostMedia> media = new LinkedList<>();

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    public void addPostMedia(PostMedia postMedia) {
        this.media.add(postMedia);
    }

    public void removePostMedia(PostMedia postMedia) {
        this.media.remove(postMedia);
    }
}
