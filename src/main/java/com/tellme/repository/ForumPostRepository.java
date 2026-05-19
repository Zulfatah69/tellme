package com.tellme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tellme.model.ForumPost;

public interface ForumPostRepository
        extends JpaRepository<ForumPost, Long> {

    @Query(value = """

        SELECT fp.*
        FROM forum_post fp

        LEFT JOIN forum_comment fc
            ON fc.post_id = fp.id

        GROUP BY fp.id

        ORDER BY COUNT(fc.id) DESC,
                 fp.tanggal DESC

        """,
        nativeQuery = true)
    List<ForumPost> findAllByPopular();
}