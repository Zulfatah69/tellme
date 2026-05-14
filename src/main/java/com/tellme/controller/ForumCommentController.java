package com.tellme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.dto.ForumCommentResponse;
import com.tellme.model.ForumComment;
import com.tellme.service.interfaces.ForumCommentService;

@RestController
@RequestMapping("/api/forum-comment")
public class ForumCommentController {

    @Autowired
    private ForumCommentService forumCommentService;

    @PostMapping
    public ForumComment createComment(
            @RequestBody ForumComment comment) {

        return forumCommentService.createComment(comment);
    }

    @GetMapping("/{postId}")
    public List<ForumCommentResponse> getByPost(
            @PathVariable Long postId) {

        return forumCommentService.getByPost(postId);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable Long id) {

        forumCommentService.deleteComment(id);
    }
}