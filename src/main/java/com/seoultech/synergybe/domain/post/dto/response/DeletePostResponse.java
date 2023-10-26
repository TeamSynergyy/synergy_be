package com.seoultech.synergybe.domain.post.dto.response;

import com.seoultech.synergybe.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DeletePostResponse {
    private Long postId;

    public static DeletePostResponse from(Post post) {
        return DeletePostResponse.builder().postId(post.getId()).build();
    }
}
