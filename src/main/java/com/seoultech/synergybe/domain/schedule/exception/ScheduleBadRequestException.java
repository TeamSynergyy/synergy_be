package com.seoultech.synergybe.domain.schedule.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class ScheduleBadRequestException extends BadRequestException {
    public ScheduleBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
