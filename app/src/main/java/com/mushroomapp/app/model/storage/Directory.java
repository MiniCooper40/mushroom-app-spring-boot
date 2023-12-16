package com.mushroomapp.app.model.storage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "directory_id", nullable = false)
    private UUID id;

    @Column(name = "directory_path")
    private String path;

    @OneToMany(mappedBy = "directory")
    private List<Media> media = new LinkedList<>();

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void removeMedia(Media media) {
        this.media.remove(media);
    }

}
