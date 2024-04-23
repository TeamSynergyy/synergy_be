package com.seoultech.synergybe.domain.post.dto.request;

import com.seoultech.synergybe.domain.post.Post;
import com.seoultech.synergybe.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;


public record UpdatePostRequest(
        @NotBlank(message = "게시글 제목은 필수항목입니다.")
        String title,
        @NotBlank(message = "게시글 내용은 필수항목입니다.")
        String content
) {

//    public Post toEntity(User user) {
//        return Post.builder()
//                .user(user)
//                .title(title)
//                .content(content)
//                .build();
//    }
}
