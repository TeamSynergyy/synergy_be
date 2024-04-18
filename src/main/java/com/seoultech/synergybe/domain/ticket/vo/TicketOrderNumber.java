package com.seoultech.synergybe.domain.ticket.vo;

import com.seoultech.synergybe.domain.ticket.exception.TicketBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketOrderNumber {
    private static final int MIN_NUMBER = 0;


    @Column(name = "order_number")
    private int orderNumber;

    public TicketOrderNumber(int value) {
        validateNumber(value);
        this.orderNumber = value;
    }

    private void validateNumber(int orderNumber) {
        if (MIN_NUMBER > orderNumber) {
            throw new TicketBadRequestException(
                    MessageFormat.format("티켓 위치는 {0} 보다 커야합니다.", MIN_NUMBER));
        }
    }
}
