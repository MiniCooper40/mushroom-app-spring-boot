package com.mushroomapp.app.model.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mushroomapp.app.model.storage.Media;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class PostMediaId implements Serializable {

    @Serial
    private static final long serialVersionUID = -5133735270861186460L;

    @Column(name = "post_id")
    @JsonProperty("post_id")
    private UUID postId;

    @Column(name = "media_id")
    @JsonProperty("media_id")
    private UUID mediaId;
//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    @OneToOne
//    @JoinColumn(name = "media_id")
//    private Media media;
}
