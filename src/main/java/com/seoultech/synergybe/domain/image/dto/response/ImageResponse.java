package com.seoultech.synergybe.domain.image.dto.response;

import com.seoultech.synergybe.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageResponse {
    private Long imageId;

    public static ImageResponse from(Image image) {
        return new ImageResponse(image.getId());
    }
}
