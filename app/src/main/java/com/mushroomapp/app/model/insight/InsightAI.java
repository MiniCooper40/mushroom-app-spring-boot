package com.mushroomapp.app.model.insight;

import com.mushroomapp.app.model.mushroom.Mushroom;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Media;
import jakarta.persistence.*;

@Entity
@IdClass(InsightAIId.class)
public class InsightAI {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "mushroom_id")
    private Mushroom mushroom;

    @Id
    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;
}
