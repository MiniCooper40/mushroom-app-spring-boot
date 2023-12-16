package com.mushroomapp.app.model.content;

import com.mushroomapp.app.model.storage.Media;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@IdClass(PostMediaId.class)
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostMedia {

    @Id
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "post_id")
    private Post post;

    @Id
    @OneToOne
    @PrimaryKeyJoinColumn(name = "media_id")
    private Media media;

    @Column(name = "position")
    private int position;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

}
