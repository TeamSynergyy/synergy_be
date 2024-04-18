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
public class TicketContent {
    @Column(name = "content", nullable = false)
    private String content;

    public TicketContent(String value) {
        validateNotNull(value);
        this.content = value;
    }

    private void validateNotNull(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw new TicketBadRequestException("티켓 내용은 필수 항목입니다.");
        }
    }
}
