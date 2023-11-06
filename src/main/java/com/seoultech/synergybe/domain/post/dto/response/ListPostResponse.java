package com.seoultech.synergybe.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ListPostResponse {
    private List<PostResponse> content;
    private boolean isNext;

    public static ListPostResponse from(List<PostResponse> postResponses, boolean isNext) {
        return new ListPostResponse(postResponses, isNext);
    }

    public static ListPostResponse from(List<PostResponse> postResponses) {
        return new ListPostResponse(postResponses);
    }

    public ListPostResponse(List<PostResponse> postResponses) {
        this.content = postResponses;
    }
}
