package com.seoultech.synergybe.domain.rate.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class RateBadRequestException extends BadRequestException {
    public RateBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
