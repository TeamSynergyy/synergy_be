package com.seoultech.synergybe.domain.post.business.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdatePostDto(
        String postId,
        String title,
        String content,
        List<MultipartFile> files
) {
}
