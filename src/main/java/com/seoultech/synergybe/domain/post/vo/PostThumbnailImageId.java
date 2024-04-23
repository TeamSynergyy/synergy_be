package com.seoultech.synergybe.domain.post.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostThumbnailImageId {

    @Column(name = "thumbnail_image_id", nullable = true)
    private String thumbnailImageId;

    public PostThumbnailImageId(String thumbnailImageId) {
        this.thumbnailImageId = thumbnailImageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostThumbnailImageId that = (PostThumbnailImageId) o;
        return Objects.equals(thumbnailImageId, that.thumbnailImageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thumbnailImageId);
    }
}
