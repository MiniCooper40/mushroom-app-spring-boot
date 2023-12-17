package com.mushroomapp.app.service;

import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.content.PostMediaId;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.repository.PostMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostMediaService {

    @Autowired
    private PostMediaRepository postMediaRepository;

    public PostMedia save(PostMedia postMedia) {
        return this.postMediaRepository.save(postMedia);
    }

    public PostMedia addMediaToPost(Media media, Post post) {
        PostMedia postMedia = new PostMedia();

        postMedia.setMedia(media);
        postMedia.setPost(post);

//        media.setPostMedia(postMedia); // DO THESE NOT HAVE UUID'S???
        post.addPostMedia(postMedia);

        return postMediaRepository.save(postMedia); // JpaSystemException: attempted to assign id from null one-to-one property
    }
}
