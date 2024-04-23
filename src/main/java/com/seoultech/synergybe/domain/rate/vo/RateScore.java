package com.seoultech.synergybe.domain.rate.vo;

import com.seoultech.synergybe.domain.rate.exception.RateBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RateScore {
    private static final int MAX_SCORE_LENGTH = 5;
    private static final int MIN_SCORE_LENGTH = 1;

    @Column(name = "score", nullable = false)
    private int score;

    public RateScore(Integer value) {
        validateNotNull(value);
        validateRateScoreLength(value);
        this.score = value;
    }

    private void validateNotNull(Integer value) {
        if (value == null) {
            throw new RateBadRequestException("Score은 필수 항목입니다.");
        }
    }

    private void validateRateScoreLength(Integer value) {
        if (value < MIN_SCORE_LENGTH || value > MAX_SCORE_LENGTH) {
            throw new RateBadRequestException(
                    MessageFormat.format("평점은 {0} 이상 {1} 이하여야 합니다.",
                            MIN_SCORE_LENGTH, MAX_SCORE_LENGTH
                    ));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateScore rateScore = (RateScore) o;
        return score == rateScore.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score);
    }
}
