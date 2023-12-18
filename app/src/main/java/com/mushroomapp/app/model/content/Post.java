package com.mushroomapp.app.model.content;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Setter
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id", nullable = false)
    @JsonProperty("post_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnoreProperties
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Like> likes = new LinkedList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments = new LinkedList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
//    @JsonIgnore
    @JsonProperty("post_media")
    private List<PostMedia> media = new LinkedList<>();

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Transactional
    public void addPostMedia(PostMedia postMedia) {
        this.media.add(postMedia);
    }

    @Transactional
    public void removePostMedia(PostMedia postMedia) {
        this.media.remove(postMedia);
    }

    @Transactional
    public void addLike(Like like) {
        this.likes.add(like);
    }

    @Transactional
    public void removeLike(Like like) {
        this.likes.remove(like);
    }

    @Transactional
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Transactional
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}
