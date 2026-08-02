package com.tellme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tellme.model.ForumPost;

/**
 * Spring Data JPA repository for {@link ForumPost} entities.
 *
 * <p>Includes a custom native query to retrieve posts ordered by
 * their comment count for the "popular" feed view.
 */
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    /**
     * Returns all forum posts ordered by comment count (descending), then by
     * creation date (descending) as a tiebreaker.
     *
     * @return list of forum posts sorted by popularity
     */
    @Query(value = """
            SELECT fp.*
            FROM forum_post fp
            LEFT JOIN forum_comment fc ON fc.post_id = fp.id
            GROUP BY fp.id
            ORDER BY COUNT(fc.id) DESC, fp.tanggal DESC
            """,
            nativeQuery = true)
    List<ForumPost> findAllByPopular();

    /**
     * Deletes all forum posts authored by the specified user.
     * Used during account deletion to cascade-remove user content.
     *
     * @param userId the author's primary key
     */
    void deleteByUserId(Long userId);
}