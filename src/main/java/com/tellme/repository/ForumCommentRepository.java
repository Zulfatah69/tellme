package com.tellme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.ForumComment;

/**
 * Spring Data JPA repository for {@link ForumComment} entities.
 *
 * <p>Supports threaded comment retrieval by separating top-level comments
 * from replies (comments that have a parent comment reference).
 */
public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {

    /**
     * Returns all top-level comments for a given post (i.e., comments with
     * no parent comment), suitable for building a threaded view.
     *
     * @param postId the forum post's primary key
     * @return list of top-level comments for the post
     */
    List<ForumComment> findByPostIdAndParentCommentIsNull(Long postId);

    /**
     * Returns all direct replies to a given parent comment.
     *
     * @param parentId the parent comment's primary key
     * @return list of reply comments
     */
    List<ForumComment> findByParentCommentId(Long parentId);

    /**
     * Deletes all comments authored by the specified user.
     * Used during account deletion to cascade-remove user content.
     *
     * @param userId the author's primary key
     */
    void deleteByUserId(Long userId);
}