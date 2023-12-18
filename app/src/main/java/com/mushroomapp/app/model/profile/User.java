package com.mushroomapp.app.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.interaction.Like;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private UUID id;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "follower",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<User> following = new LinkedList<>();

    @ManyToMany(mappedBy = "following")
    @ToString.Exclude
    @JsonIgnore
    private List<User> followers = new LinkedList<>();

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<Comment> comments;

    @Transactional
    public void follow(User other) {
        this.following.add(other);
    }

    @Transactional
    public void unfollow(User other) {
        this.following.remove(other);
    }

    @Transactional
    public void addFollower(User follower) {
        this.followers.add(follower);
    }

    @Transactional
    public void removeFollower(User follower) {
        this.followers.remove(follower);
    }

    @Transactional
    public void addPost(Post post) {
        this.posts.add(post);
    }

    @Transactional
    public void removePost(Post post) {
        this.posts.remove(post);
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
