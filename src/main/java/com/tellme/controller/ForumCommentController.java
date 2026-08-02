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

import com.tellme.dto.ForumCommentResponse;
import com.tellme.model.ForumComment;
import com.tellme.service.interfaces.ForumCommentService;

/**
 * REST controller for forum comments and threaded replies.
 *
 * <p>Comments belong to a {@link com.tellme.model.ForumPost} and optionally
 * reference a parent comment to form one level of threading.
 */
@RestController
@RequestMapping("/api/forum-comment")
public class ForumCommentController {

    private final ForumCommentService forumCommentService;

    public ForumCommentController(ForumCommentService forumCommentService) {
        this.forumCommentService = forumCommentService;
    }

    /**
     * Creates a new comment or reply on a forum post.
     *
     * @param comment the comment payload (must include {@code post.id} and {@code user.id})
     * @return the created comment with HTTP 201
     */
    @PostMapping
    public ResponseEntity<ForumComment> createComment(@RequestBody ForumComment comment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(forumCommentService.createComment(comment));
    }

    /**
     * Returns all top-level comments for a given post, with nested replies.
     *
     * @param postId the forum post ID
     * @return list of comment trees
     */
    @GetMapping("/{postId}")
    public ResponseEntity<List<ForumCommentResponse>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(forumCommentService.getByPost(postId));
    }

    /**
     * Deletes a comment by ID.
     *
     * @param id the comment ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        forumCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}