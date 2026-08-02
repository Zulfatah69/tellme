package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.dto.ForumCommentResponse;
import com.tellme.model.ForumComment;

/**
 * Service contract for forum comment and reply operations.
 */
public interface ForumCommentService {

    /**
     * Creates a new comment or reply on a forum post.
     *
     * @param comment the comment payload; must include {@code post.id} and {@code user.id}.
     *                Set {@code parentComment.id} to create a reply.
     * @return the persisted comment
     */
    ForumComment createComment(ForumComment comment);

    /**
     * Returns all top-level comments for a given post, each with
     * their nested replies and total reply count.
     *
     * @param postId the forum post's primary key
     * @return list of comment response trees (top-level comments with replies)
     */
    List<ForumCommentResponse> getByPost(Long postId);

    /**
     * Deletes a comment by ID.
     *
     * @param id the comment's primary key
     */
    void deleteComment(Long id);
}