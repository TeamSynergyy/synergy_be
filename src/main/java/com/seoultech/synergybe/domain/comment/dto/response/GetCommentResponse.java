package com.seoultech.synergybe.domain.comment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetCommentResponse(
    Long commentId,
    String userId,
    Long postId,
    String comment,
    LocalDateTime updateAt
) {
}
//    public static CommentResponse from(Comment comment) {
//        return new CommentResponse(comment.getId(), comment.getUser().getUserId(), comment.getPost().getId(), comment.getComment(), comment.getUpdateAt());
//    }
//
//    public static List<CommentResponse> from(List<Comment> comments) {
//        return comments.stream()
//                .map(comment -> CommentResponse.builder()
//                        .commentId(comment.getId())
//                        .userId(comment.getUser().getUserId())
//                        .postId(comment.getPost().getId())
//                        .comment(comment.getComment())
//                        .updateAt(comment.getUpdateAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
