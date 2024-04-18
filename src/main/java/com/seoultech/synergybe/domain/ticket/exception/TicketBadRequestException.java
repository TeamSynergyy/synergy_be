package com.seoultech.synergybe.domain.ticket.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class TicketBadRequestException extends BadRequestException {
    public TicketBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
