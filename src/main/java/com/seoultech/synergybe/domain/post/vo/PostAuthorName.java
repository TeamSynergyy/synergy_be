package com.seoultech.synergybe.domain.post.vo;

import com.seoultech.synergybe.domain.post.exception.PostBadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAuthorName {
    @Column(name = "author_name")
    private String authorName;

    public PostAuthorName(String value) {
        validateNotNull(value);
        this.authorName = value;
    }

    private void validateNotNull(String authorName) {
        if (Objects.isNull(authorName) || authorName.isBlank()) {
            throw new PostBadRequestException("게시글의 저자 이름은 필수 항목입니다.");
        }
    }
}
