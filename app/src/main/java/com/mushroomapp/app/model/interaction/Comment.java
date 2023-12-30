package com.mushroomapp.app.model.interaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mushroomapp.app.controller.format.serialize.CommentSerializer;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.content.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonSerialize(using = CommentSerializer.class)
//@IdClass(InteractionId.class)
public class Comment {

//    @EmbeddedId
//    private InteractionId id = new InteractionId();

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id")
    private UUID id;

    //    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    //@MapsId("userId")
    @JsonIgnore
    private User user;

    //    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    //@MapsId("postId")
    @JsonIgnore
    private Post post;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "respondedTo")
    @ToString.Exclude
    private List<Comment> responses = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "comment_responded_to_id")
    private Comment respondedTo;

    public void addResponse(Comment comment) {
        this.responses.add(comment);
    }

    public void deleteResponse(Comment comment) {
        this.responses.remove(comment);
    }

}

