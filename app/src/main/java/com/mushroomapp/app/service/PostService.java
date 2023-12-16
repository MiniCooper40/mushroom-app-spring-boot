package com.mushroomapp.app.service;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.repository.PostMediaRepository;
import com.mushroomapp.app.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMediaService postMediaService;

    @Transactional
    public Post createPost(User user, List<Media> media, String caption) {
        Post post = new Post();
        post.setUser(user);

        for(Media m : media) postMediaService.addMediaToPost(m, post);

        return this.postRepository.save(post);
    }

}
