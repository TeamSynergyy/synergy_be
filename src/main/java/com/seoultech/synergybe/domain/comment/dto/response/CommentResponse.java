package com.seoultech.synergybe.domain.comment.dto.response;

import com.seoultech.synergybe.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private String userId;
    private Long postId;
    private String comment;
    private LocalDateTime updateAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getUser().getUserId(), comment.getPost().getId(), comment.getComment(), comment.getUpdateAt());
    }

    public static List<CommentResponse> from(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .commentId(comment.getId())
                        .userId(comment.getUser().getUserId())
                        .postId(comment.getPost().getId())
                        .comment(comment.getComment())
                        .updateAt(comment.getUpdateAt())
                        .build())
                .collect(Collectors.toList());
    }
}
