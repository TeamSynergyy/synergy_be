package com.seoultech.synergybe.domain.comment.dto.response;

import com.seoultech.synergybe.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class CommentResponse {
    private String userId;
    private Long postId;
    private String comment;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getUser().getUserId(), comment.getPost().getId(), comment.getComment());
    }

    public static List<CommentResponse> from(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .userId(comment.getUser().getUserId())
                        .postId(comment.getPost().getId())
                        .comment(comment.getComment())
                        .build())
                .collect(Collectors.toList());
    }
}
