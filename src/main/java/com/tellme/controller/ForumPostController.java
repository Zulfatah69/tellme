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

import com.tellme.model.ForumPost;
import com.tellme.service.interfaces.ForumPostService;

@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @PostMapping
    public ForumPost createPost(@RequestBody ForumPost post) {
        return forumPostService.createPost(post);
    }

    @GetMapping
    public List<ForumPost> getAllPost() {
        return forumPostService.getAllPost();
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        forumPostService.deletePost(id);
    }
}