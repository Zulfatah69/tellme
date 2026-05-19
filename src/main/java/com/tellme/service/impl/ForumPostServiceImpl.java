package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellme.model.ForumPost;
import com.tellme.model.User;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.ForumPostService;

@Service
public class ForumPostServiceImpl
        implements ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ForumPost createPost(
            ForumPost post) {

        User user =
                userRepository.findById(
                        post.getUser().getId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User tidak ditemukan"
                        )
                );

        post.setUser(user);

        post.setTanggal(
                LocalDateTime.now()
        );

        return forumPostRepository.save(post);
    }

    @Override
    public List<ForumPost> getAllPost() {

        return forumPostRepository
                .findAllByPopular();
    }

    @Override
    public void deletePost(Long id) {

        forumPostRepository.deleteById(id);
    }
}