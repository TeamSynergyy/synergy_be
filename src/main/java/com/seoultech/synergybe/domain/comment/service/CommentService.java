package com.seoultech.synergybe.domain.comment.service;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.comment.dto.request.CommentRequest;
import com.seoultech.synergybe.domain.comment.dto.response.CommentResponse;
import com.seoultech.synergybe.domain.comment.repository.CommentRepository;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    private final NotificationService notificationService;

    public CommentResponse createComment(User user, CommentRequest request) {
        Post post = postService.findPostById(request.getPostId());
        Comment savedComment = commentRepository.save(request.toEntity(user, post, request.getComment()));
        savedComment.addPost(post);
        User postUser = post.getUser();
//        notificationService.send(postUser, NotificationType.COMMENT, "댓글이 생성되었습니다", post.getId());

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

    public List<CommentResponse> getCommentList(Long postId) {
        List<Long> commentIds = commentRepository.findCommentIdsByPostId(postId);

        List<Comment> comments = commentRepository.findAllById(commentIds);

        return CommentResponse.from(comments);
    }
}
