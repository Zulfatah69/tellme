package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.model.ForumPost;

public interface ForumPostService {

    ForumPost createPost(ForumPost post);

    List<ForumPost> getAllPost();

    void deletePost(Long id);
}