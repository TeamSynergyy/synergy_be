package com.seoultech.synergybe.domain.image.dto.request;

import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class CreateImageRequest {
    private MultipartFile file;
    private String imageUrl;
    private Long postId;

    public Image toEntity(Post post, String imageUrl) {
        return Image.builder()
                .build();
    }
}
