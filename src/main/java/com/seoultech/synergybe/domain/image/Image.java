package com.seoultech.synergybe.domain.image;

import com.seoultech.synergybe.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String originalFilename;

    private String storeFileName;

    @Builder
    public Image(String originalFilename, String storeFileName) {
        this.originalFilename = originalFilename;
        this.storeFileName = storeFileName;
    }
}
