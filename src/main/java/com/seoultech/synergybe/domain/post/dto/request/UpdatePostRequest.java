package com.seoultech.synergybe.domain.post.dto.request;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePostRequest {
    private Long postId;
    private String title;
    private String content;

    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
