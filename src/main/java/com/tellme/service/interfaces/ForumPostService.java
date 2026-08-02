package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.model.ForumPost;

/**
 * Service contract for forum discussion post operations.
 */
public interface ForumPostService {

    /**
     * Creates a new forum post authored by the specified user.
     *
     * @param post the post content and author reference (must include {@code user.id})
     * @return the persisted forum post
     */
    ForumPost createPost(ForumPost post);

    /**
     * Returns all forum posts ordered by popularity (most commented first),
     * then by creation date (newest first) as a tiebreaker.
     *
     * @return list of forum posts
     */
    List<ForumPost> getAllPost();

    /**
     * Deletes a forum post and all its associated comments (cascade).
     *
     * @param id the post's primary key
     */
    void deletePost(Long id);
}