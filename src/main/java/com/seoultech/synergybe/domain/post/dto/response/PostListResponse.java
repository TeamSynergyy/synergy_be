package com.seoultech.synergybe.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostListResponse {
    private List<PostResponse> content;
    private boolean isNext;

    public static PostListResponse from(List<PostResponse> postResponses, boolean isNext) {
        return new PostListResponse(postResponses, isNext);
    }
}
