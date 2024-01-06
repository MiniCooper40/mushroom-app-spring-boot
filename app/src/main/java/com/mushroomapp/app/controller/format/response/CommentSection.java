package com.mushroomapp.app.controller.format.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mushroomapp.app.controller.format.serialize.CommentSectionSerializer;
import com.mushroomapp.app.model.interaction.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonSerialize(using = CommentSectionSerializer.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentSection {


    public List<Comment> comments;
}
