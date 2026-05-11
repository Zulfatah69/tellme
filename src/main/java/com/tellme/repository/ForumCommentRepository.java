package com.tellme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.ForumComment;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {

    List<ForumComment> findByPostId(Long postId);
}