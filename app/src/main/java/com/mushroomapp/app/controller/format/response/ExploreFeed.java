package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mushroomapp.app.controller.format.serialize.PostListSerializer;
import com.mushroomapp.app.model.content.Post;

import java.util.List;

public class ExploreFeed {

    @JsonSerialize(using = PostListSerializer.class)
    private final List<Post> posts;

    public ExploreFeed(List<Post> posts) {
        this.posts = posts;
    }
}
