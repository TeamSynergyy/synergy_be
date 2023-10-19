package com.seoultech.synergybe.domain.postlike.dto.response;

import com.seoultech.synergybe.domain.postlike.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostLikeResponse {
    private String userId;
    private Long postId;

    public static PostLikeResponse from(PostLike postLike) {
        return new PostLikeResponse(postLike.getUser().getUserId(), postLike.getPost().getId());
    }
}
