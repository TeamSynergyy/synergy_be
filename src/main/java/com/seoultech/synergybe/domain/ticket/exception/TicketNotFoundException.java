package com.seoultech.synergybe.domain.ticket.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
