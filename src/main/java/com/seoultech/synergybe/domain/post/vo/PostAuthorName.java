package com.seoultech.synergybe.domain.post.vo;

import com.seoultech.synergybe.domain.post.exception.PostBadRequestException;
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
        if (authorName == null || authorName.isBlank()) {
            throw new PostBadRequestException("게시글의 저자 이름은 필수 항목입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostAuthorName that = (PostAuthorName) o;
        return Objects.equals(authorName, that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName);
    }
}
