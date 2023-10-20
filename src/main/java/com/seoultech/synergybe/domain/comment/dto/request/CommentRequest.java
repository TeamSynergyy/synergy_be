package com.seoultech.synergybe.domain.comment.dto.request;

import com.seoultech.synergybe.domain.comment.Comment;
import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentRequest {
    private String userId;
    private Long postId;
    private Long commentId;
    private String comment;

    public Comment toEntity(User user, Post post, String comment) {
        return Comment.builder()
                .user(user)
                .post(post)
                .comment(comment)
                .build();
    }
}
