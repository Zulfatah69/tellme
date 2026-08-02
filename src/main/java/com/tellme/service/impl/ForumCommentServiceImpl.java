package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tellme.dto.ForumCommentResponse;
import com.tellme.exception.ResourceNotFoundException;
import com.tellme.model.ForumComment;
import com.tellme.model.ForumPost;
import com.tellme.model.User;
import com.tellme.repository.ForumCommentRepository;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.ForumCommentService;

/**
 * Default implementation of {@link ForumCommentService}.
 *
 * <p>Handles creation and retrieval of forum comments, including
 * threaded replies (one level of nesting). Each comment response
 * includes its direct replies and total reply count.
 */
@Service
public class ForumCommentServiceImpl implements ForumCommentService {

    private static final Logger log = LoggerFactory.getLogger(ForumCommentServiceImpl.class);

    private final ForumCommentRepository forumCommentRepository;
    private final ForumPostRepository forumPostRepository;
    private final UserRepository userRepository;

    public ForumCommentServiceImpl(ForumCommentRepository forumCommentRepository,
                                   ForumPostRepository forumPostRepository,
                                   UserRepository userRepository) {
        this.forumCommentRepository = forumCommentRepository;
        this.forumPostRepository = forumPostRepository;
        this.userRepository = userRepository;
    }

    /** {@inheritDoc} */
    @Override
    public ForumComment createComment(ForumComment comment) {
        ForumPost post = forumPostRepository.findById(comment.getPost().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Forum post", comment.getPost().getId()));

        User user = userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", comment.getUser().getId()));

        comment.setPost(post);
        comment.setUser(user);
        comment.setTanggal(LocalDateTime.now());

        if (comment.getParentComment() != null && comment.getParentComment().getId() != null) {
            ForumComment parent = forumCommentRepository.findById(comment.getParentComment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment", comment.getParentComment().getId()));
            comment.setParentComment(parent);
        }

        ForumComment saved = forumCommentRepository.save(comment);
        log.info("Forum comment created: id={}, postId={}, userId={}", saved.getId(), post.getId(), user.getId());
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public List<ForumCommentResponse> getByPost(Long postId) {
        return forumCommentRepository.findByPostIdAndParentCommentIsNull(postId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public void deleteComment(Long id) {
        if (!forumCommentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Forum comment", id);
        }
        forumCommentRepository.deleteById(id);
        log.info("Forum comment {} deleted", id);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Maps a {@link ForumComment} entity to its DTO representation,
     * including all direct replies recursively.
     */
    private ForumCommentResponse mapToResponse(ForumComment comment) {
        ForumCommentResponse response = new ForumCommentResponse();
        response.setId(comment.getId());
        response.setIsiKomentar(comment.getIsiKomentar());
        response.setTanggal(comment.getTanggal());
        response.setUserId(comment.getUser().getId());
        response.setNamaUser(comment.getUser().getNama());

        if (comment.getParentComment() != null) {
            response.setParentCommentId(comment.getParentComment().getId());
        }

        List<ForumCommentResponse> replies = forumCommentRepository
                .findByParentCommentId(comment.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        response.setTotalReply(replies.size());
        response.setReplies(replies);
        return response;
    }
}