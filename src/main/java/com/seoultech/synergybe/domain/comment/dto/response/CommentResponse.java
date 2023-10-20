package com.seoultech.synergybe.domain.comment.dto.response;

import com.seoultech.synergybe.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponse {
    private String userId;
    private Long postId;
    private String comment;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getUser().getUserId(), comment.getPost().getId(), comment.getComment());
    }
}
