package com.mushroomapp.app.model.profile;

import com.mushroomapp.app.model.content.Post;
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

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "follower",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    @ToString.Exclude
    private List<User> following = new LinkedList<>();

    @ManyToMany(mappedBy = "following")
    @ToString.Exclude
    private List<User> followers = new LinkedList<>();

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

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
}
