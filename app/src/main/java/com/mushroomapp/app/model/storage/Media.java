package com.mushroomapp.app.model.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mushroomapp.app.model.insight.AiInsight;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@ToString
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "media_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    @ToString.Exclude
    @JsonIgnore
    private Directory directory;


    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(name = "filename")
    private String filename;

    @OneToOne
    @JoinColumn(name = "ai_insight_id")
    private AiInsight aiInsight;

    @Transient
    public String getPath() {
        return this.directory.getPath() + this.getFilename();
    }
}
