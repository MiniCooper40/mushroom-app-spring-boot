package com.mushroomapp.app.service;

import com.mushroomapp.app.controller.format.response.ExploreFeed;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.content.PostMedia;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private PostMediaService postMediaService;

    @Transactional
    public Post createPost(User user, List<Media> media, String caption) {
        Post post = new Post();
        post.setCaption(caption);
        post.setUser(user);
        postRepository.save(post);

        user.addPost(post);

        for(Media m : media) {
            mediaService.save(m);
            PostMedia postMedia = new PostMedia();
            postMedia.setPost(post);
            postMedia.setMedia(m);

            System.out.println("Post media: " + postMedia);
            postMediaService.save(postMedia);
        }

        return this.postRepository.save(post);
    }

    public List<Post> findMostRecent() {
        return this.postRepository.findTop10ByOrderByTimestampDesc();
    }

    @Transactional
    public Optional<Post> findPostById(UUID postId) {
        return this.postRepository.findById(postId);
    }

    public void deletePostById(UUID postId) {
        this.postRepository.deleteById(postId);
    }

}
