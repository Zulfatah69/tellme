package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellme.model.ForumComment;
import com.tellme.model.ForumPost;
import com.tellme.model.User;
import com.tellme.repository.ForumCommentRepository;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.ForumCommentService;

@Service
public class ForumCommentServiceImpl implements ForumCommentService {

    @Autowired
    private ForumCommentRepository forumCommentRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ForumComment createComment(ForumComment comment) {

        ForumPost post = forumPostRepository.findById(comment.getPost().getId())
                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan"));

        User user = userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        comment.setPost(post);
        comment.setUser(user);
        comment.setTanggal(LocalDateTime.now());

        return forumCommentRepository.save(comment);
    }

    @Override
    public List<ForumComment> getByPost(Long postId) {
        return forumCommentRepository.findByPostId(postId);
    }

    @Override
    public void deleteComment(Long id) {
        forumCommentRepository.deleteById(id);
    }
}