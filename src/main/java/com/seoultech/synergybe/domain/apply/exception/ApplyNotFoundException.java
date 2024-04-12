package com.seoultech.synergybe.domain.apply.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class ApplyNotFoundException extends NotFoundException {
    public ApplyNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
