package com.seoultech.synergybe.domain.post.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreatePostRequest(
        @NotBlank(message = "제목은 필수항목입니다.")
        String title,
        @NotBlank(message = "내용은 필수항목입니다.")
        String content,
        List<MultipartFile> files
) {

//    public Post toEntity(User user, List<Image> images) {
//        List<PostLike> likes = new ArrayList<>();
//        return Post.builder()
//                .user(user)
//                .thumbnailImageId(images.get(0).getId())
//                .images(images)
//                .title(title)
//                .content(content)
//                .likes(likes)
//                .build();
//    }
//
//    public Post toEntity(User user) {
//        List<PostLike> likes = new ArrayList<>();
//        return Post.builder()
//                .user(user)
//                .title(title)
//                .content(content)
//                .likes(likes)
//                .build();
//    }
}
