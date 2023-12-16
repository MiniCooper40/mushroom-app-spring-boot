package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.content.PostMediaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMediaRepository extends JpaRepository<PostMedia, PostMediaId> {

}
