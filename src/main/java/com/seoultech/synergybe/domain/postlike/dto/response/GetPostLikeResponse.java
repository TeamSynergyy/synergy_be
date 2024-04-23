package com.seoultech.synergybe.domain.postlike.dto.response;

import com.seoultech.synergybe.domain.postlike.PostLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetPostLikeResponse(
        String userId,
        String postId
) {

//    public static GetPostLikeResponse from(PostLike postLike) {
//        return new GetPostLikeResponse(postLike.getUser().getUserId(), postLike.getPost().getId());
//    }
}
