package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellme.dto.ForumCommentResponse;
import com.tellme.model.ForumComment;
import com.tellme.model.ForumPost;
import com.tellme.model.User;
import com.tellme.repository.ForumCommentRepository;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.ForumCommentService;

@Service
public class ForumCommentServiceImpl
        implements ForumCommentService {

    @Autowired
    private ForumCommentRepository forumCommentRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ForumComment createComment(
            ForumComment comment) {

        ForumPost post =
                forumPostRepository.findById(
                        comment.getPost().getId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Post tidak ditemukan"
                        )
                );

        User user =
                userRepository.findById(
                        comment.getUser().getId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User tidak ditemukan"
                        )
                );

        comment.setPost(post);

        comment.setUser(user);

        comment.setTanggal(LocalDateTime.now());

        if(comment.getParentComment() != null){

            ForumComment parent =
                    forumCommentRepository.findById(
                            comment.getParentComment().getId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Komentar parent tidak ditemukan"
                            )
                    );

            comment.setParentComment(parent);
        }

        return forumCommentRepository.save(comment);
    }

    @Override
    public List<ForumCommentResponse> getByPost(
            Long postId) {

        List<ForumComment> comments =
                forumCommentRepository
                        .findByPostIdAndParentCommentIsNull(
                                postId
                        );

        List<ForumCommentResponse> responses =
                new ArrayList<>();

        for(ForumComment comment : comments){

            responses.add(
                    mapToResponse(comment)
            );
        }

        return responses;
    }

    private ForumCommentResponse mapToResponse(
            ForumComment comment){

        ForumCommentResponse response =
                new ForumCommentResponse();

        response.setId(comment.getId());

        response.setIsiKomentar(
                comment.getIsiKomentar()
        );

        response.setTanggal(
                comment.getTanggal()
        );

        response.setUserId(
                comment.getUser().getId()
        );

        response.setNamaUser(
                comment.getUser().getNama()
        );

        if(comment.getParentComment() != null){

            response.setParentCommentId(
                    comment.getParentComment().getId()
            );
        }

        List<ForumComment> replies =
                forumCommentRepository
                        .findByParentCommentId(
                                comment.getId()
                        );

        response.setTotalReply(
                replies.size()
        );

        List<ForumCommentResponse> replyResponses =
                new ArrayList<>();

        for(ForumComment reply : replies){

            replyResponses.add(
                    mapToResponse(reply)
            );
        }

        response.setReplies(replyResponses);

        return response;
    }

    @Override
    public void deleteComment(Long id) {

        forumCommentRepository.deleteById(id);
    }
}