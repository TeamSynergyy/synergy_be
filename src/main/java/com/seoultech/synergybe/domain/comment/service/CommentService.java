package com.seoultech.synergybe.domain.comment.service;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.comment.dto.request.CreateCommentRequest;
import com.seoultech.synergybe.domain.comment.dto.request.UpdateCommentRequest;
import com.seoultech.synergybe.domain.comment.dto.response.GetCommentResponse;
import com.seoultech.synergybe.domain.comment.exception.CommentNotFoundException;
import com.seoultech.synergybe.domain.comment.repository.CommentRepository;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.post.implement.PostReader;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostReader postReader;
    private final UserService userService;
    private final NotificationService notificationService;
    private final IdGenerator idGenerator;
    private final TokenGenerator tokenGenerator;

    public GetCommentResponse createComment(String userId, CreateCommentRequest request) {
        Post post = postReader.read(request.postId());
        User user = userService.getUserByToken(userId);
        Long commentId = idGenerator.generateId();
        String commentToken = tokenGenerator.generateToken(IdPrefix.COMMENT);

        Comment comment = Comment.builder()
                .id(commentId)
                .commentToken(commentToken)
                .comment(request.comment())
                .post(post).user(user)
                .build();

        Comment savedComment = commentRepository.save(comment);
        savedComment.addPost(post);
//        User postUser = post.getUser();
//        notificationService.send(postUser, NotificationType.COMMENT, "댓글이 생성되었습니다", post.getId());
        return GetCommentResponse.builder()
                .commentId(savedComment.getCommentToken())
                .build();
    }

    public GetCommentResponse updateComment(UpdateCommentRequest request) {
        Comment comment = this.findCommentById(request.commentId());
        Comment updatedComment = commentRepository.save(comment.updateComment(request));
        GetCommentResponse commentResponse = GetCommentResponse.builder().build();

        return commentResponse;
    }

    public Comment findCommentById(String commentId) {
        return commentRepository.findByCommentToken(commentId);
    }

    public void deleteComment(String commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    public Long getCommentId(String commentToken) {
        return commentRepository.findByCommentToken(commentToken).getId();
    }

    public ListResponse<GetCommentResponse> getCommentList(String postId) {
        List<String> commentTokens = commentRepository.findCommentIdsByPostId(postId);
        List<Long> commentIds = new ArrayList<>();
        for (String commentToken : commentTokens) {
            commentIds.add(getCommentId(commentToken));
        }

        List<Comment> comments = commentRepository.findAllById(commentIds);

        ListResponse<GetCommentResponse> getCommentResponses = new ListResponse(comments);

        return getCommentResponses;
    }
}
