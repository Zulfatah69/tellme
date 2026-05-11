package com.tellme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.ForumPost;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

}