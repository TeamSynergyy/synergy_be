package com.seoultech.synergybe.domain.apply.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class ApplyBadRequestException extends BadRequestException {
    public ApplyBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
