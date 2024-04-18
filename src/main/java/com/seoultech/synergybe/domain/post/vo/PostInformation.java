package com.seoultech.synergybe.domain.post.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostInformation {

    @Column(name = "thumbnail_image_id", nullable = true)
    private String thumbnailImageId;

    public PostInformation(String thumbnailImageId) {
        this.thumbnailImageId = thumbnailImageId;
    }
}
