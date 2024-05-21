package com.seoultech.synergybe.domain.post.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdatePostRequest(
        @NotBlank(message = "게시글 제목은 필수항목입니다.")
        String title,
        @NotBlank(message = "게시글 내용은 필수항목입니다.")
        String content,
        @NotBlank(message = "게시글ID는 필수항목입니다.")
        String postId,
        List<MultipartFile> files
) {
}
