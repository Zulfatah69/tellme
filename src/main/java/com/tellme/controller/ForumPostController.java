package com.tellme.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.model.ForumPost;
import com.tellme.service.interfaces.ForumPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for forum discussion posts.
 *
 * <p>Provides endpoints for creating, listing, and deleting forum posts.
 * Posts are returned ordered by popularity (most commented first).
 */
@Tag(name = "Forum Posts", description = "Student discussion forum — create, list, and delete posts")
@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService forumPostService;

    public ForumPostController(ForumPostService forumPostService) {
        this.forumPostService = forumPostService;
    }

    /**
     * Creates a new forum post.
     *
     * @param post the post content and author reference
     * @return the created post with HTTP 201
     */
    @Operation(summary = "Create a new forum post")
    @PostMapping
    public ResponseEntity<ForumPost> createPost(@RequestBody ForumPost post) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(forumPostService.createPost(post));
    }

    /**
     * Returns all forum posts ordered by popularity (comment count, descending).
     */
    @Operation(summary = "List all forum posts ordered by popularity")
    @GetMapping
    public ResponseEntity<List<ForumPost>> getAllPost() {
        return ResponseEntity.ok(forumPostService.getAllPost());
    }

    /**
     * Deletes a forum post and all its comments (cascade).
     *
     * @param id the post ID
     */
    @Operation(summary = "Delete a forum post and all its comments")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        forumPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}