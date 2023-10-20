package com.seoultech.synergybe.domain.comment.service;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.comment.dto.request.CommentRequest;
import com.seoultech.synergybe.domain.comment.dto.response.CommentResponse;
import com.seoultech.synergybe.domain.comment.repository.CommentRepository;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    public CommentResponse createComment(User user, CommentRequest request) {
        Post post = postService.findPostById(request.getPostId());
        Comment savedComment = commentRepository.save(request.toEntity(user, post, request.getComment()));

        return CommentResponse.from(savedComment);
    }

    public CommentResponse updateComment(CommentRequest request) {
        Comment comment = this.findCommentById(request.getCommentId());
        Comment updatedComment = commentRepository.save(comment.updateComment(request));

        return CommentResponse.from(updatedComment);
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NotExistCommentException::new);
    }

    public CommentResponse deleteComment(CommentRequest request) {
        Comment comment = findCommentById(request.getCommentId());
        commentRepository.delete(comment);

        return CommentResponse.from(comment);
    }


    public CommentResponse getComment(Long commentId) {
        Comment comment = this.findCommentById(commentId);

        return CommentResponse.from(comment);
    }
}
