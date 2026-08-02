package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tellme.exception.ResourceNotFoundException;
import com.tellme.model.ForumPost;
import com.tellme.model.User;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.ForumPostService;

/**
 * Default implementation of {@link ForumPostService}.
 *
 * <p>Manages creation, retrieval, and deletion of forum discussion posts.
 * Posts are ordered by comment count (most discussed first) via the
 * repository's custom query.
 */
@Service
public class ForumPostServiceImpl implements ForumPostService {

    private static final Logger log = LoggerFactory.getLogger(ForumPostServiceImpl.class);

    private final ForumPostRepository forumPostRepository;
    private final UserRepository userRepository;

    public ForumPostServiceImpl(ForumPostRepository forumPostRepository,
                                UserRepository userRepository) {
        this.forumPostRepository = forumPostRepository;
        this.userRepository = userRepository;
    }

    /** {@inheritDoc} */
    @Override
    public ForumPost createPost(ForumPost post) {
        User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", post.getUser().getId()));

        post.setUser(user);
        post.setTanggal(LocalDateTime.now());

        ForumPost saved = forumPostRepository.save(post);
        log.info("Forum post created: id={}, userId={}", saved.getId(), user.getId());
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public List<ForumPost> getAllPost() {
        return forumPostRepository.findAllByPopular();
    }

    /** {@inheritDoc} */
    @Override
    public void deletePost(Long id) {
        if (!forumPostRepository.existsById(id)) {
            throw new ResourceNotFoundException("Forum post", id);
        }
        forumPostRepository.deleteById(id);
        log.info("Forum post {} deleted", id);
    }
}