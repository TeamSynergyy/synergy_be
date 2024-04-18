package com.seoultech.synergybe.domain.ticket.vo;

import com.seoultech.synergybe.domain.ticket.exception.TicketBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketName {
    @Column(name = "title", nullable = false)
    private String title;

    public TicketName(String value) {
        validateNotNull(value);
        this.title = value;
    }

    private void validateNotNull(String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            throw new TicketBadRequestException("티켓 타이틀은 필수 항목입니다.");
        }
    }
}
