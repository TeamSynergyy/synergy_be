package com.seoultech.synergybe.domain.post.presentation.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreatePostDto(
        String title,
        String content,
        List<MultipartFile> files
) {
}
