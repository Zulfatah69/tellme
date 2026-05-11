package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.model.ForumComment;

public interface ForumCommentService {

    ForumComment createComment(ForumComment comment);

    List<ForumComment> getByPost(Long postId);

    void deleteComment(Long id);
}