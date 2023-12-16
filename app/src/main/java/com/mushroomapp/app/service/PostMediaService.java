package com.mushroomapp.app.service;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.repository.PostMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostMediaService {

    @Autowired
    private PostMediaRepository postMediaRepository;

    public PostMedia addMediaToPost(Media media, Post post) {
        PostMedia postMedia = new PostMedia();

        postMedia.setPost(post);
        postMedia.setMedia(media);

        media.setPostMedia(postMedia);
        post.addPostMedia(postMedia);

        return postMediaRepository.save(postMedia);
    }
}
