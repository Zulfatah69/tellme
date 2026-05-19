package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.dto.ForumCommentResponse;
import com.tellme.model.ForumComment;

public interface ForumCommentService {

    ForumComment createComment(
            ForumComment comment
    );

    List<ForumCommentResponse> getByPost(
            Long postId
    );

    void deleteComment(Long id);
}