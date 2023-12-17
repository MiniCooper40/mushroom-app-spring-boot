package com.mushroomapp.app.model.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mushroomapp.app.model.storage.Media;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PostMedia {

    @EmbeddedId
    @JsonProperty("post_media_id")
    private PostMediaId id = new PostMediaId();

//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "post_media_id")
//    private UUID id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @MapsId("postId")
    @JsonIgnore
    private Post post;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    @MapsId("mediaId")
    @JsonIgnore
    private Media media;

    @Column(name = "position")
    private int position;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

}
