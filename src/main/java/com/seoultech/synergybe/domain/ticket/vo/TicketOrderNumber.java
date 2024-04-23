package com.seoultech.synergybe.domain.ticket.vo;

import com.seoultech.synergybe.domain.ticket.exception.TicketBadRequestException;
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
public class TicketOrderNumber {
    private static final int MIN_NUMBER = 0;


    @Column(name = "order_number")
    private int orderNumber;

    public TicketOrderNumber(Integer value) {
        validateNumber(value);
        this.orderNumber = value;
    }

    private void validateNumber(Integer orderNumber) {
        if (orderNumber == null || MIN_NUMBER > orderNumber) {
            throw new TicketBadRequestException(
                    MessageFormat.format("티켓 위치는 {0} 보다 커야합니다.", MIN_NUMBER));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketOrderNumber that = (TicketOrderNumber) o;
        return orderNumber == that.orderNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }
}
