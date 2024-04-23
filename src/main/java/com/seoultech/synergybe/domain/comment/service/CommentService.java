package com.seoultech.synergybe.domain.comment.service;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.comment.dto.request.CreateCommentRequest;
import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.comment.exception.CommentNotFoundException;
import com.seoultech.synergybe.domain.comment.repository.CommentRepository;
import com.seoultech.synergybe.domain.common.idgenerator.IdGenerator;
import com.seoultech.synergybe.domain.common.idgenerator.IdPrefix;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.service.PostService;
import com.seoultech.synergybe.domain.user.User;
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
    private final IdGenerator idGenerator;

    public GetCommentResponse createComment(User user, CreateCommentRequest request) {
        Post post = postService.findPostById(request.getPostId());
        String commentId = idGenerator.generateId(IdPrefix.COMMENT);

        Comment comment = Comment.builder()
                .id(commentId).comment(request.getComment()).post(post).user(user)
                .build();


        Comment savedComment = commentRepository.save(comment);
        savedComment.addPost(post);
        User postUser = post.getUser();
//        notificationService.send(postUser, NotificationType.COMMENT, "댓글이 생성되었습니다", post.getId());

        return GetCommentResponse.from(savedComment);
    }

    public GetCommentResponse updateComment(CreateCommentRequest request) {
        Comment comment = this.findCommentById(request.getCommentId());
        Comment updatedComment = commentRepository.save(comment.updateComment(request));

        return GetCommentResponse.from(updatedComment);
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다."));
    }

    public GetCommentResponse deleteComment(CreateCommentRequest request) {
        Comment comment = findCommentById(request.getCommentId());
        commentRepository.delete(comment);

        return GetCommentResponse.from(comment);
    }


    public GetCommentResponse getComment(Long commentId) {
        Comment comment = this.findCommentById(commentId);

        return GetCommentResponse.from(comment);
    }

    public List<GetCommentResponse> getCommentList(Long postId) {
        List<Long> commentIds = commentRepository.findCommentIdsByPostId(postId);

        List<Comment> comments = commentRepository.findAllById(commentIds);

        return GetCommentResponse.from(comments);
    }
}
