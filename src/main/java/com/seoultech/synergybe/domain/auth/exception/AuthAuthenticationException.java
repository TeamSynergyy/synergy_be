package com.seoultech.synergybe.domain.auth.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class AuthAuthenticationException extends BadRequestException {
    public AuthAuthenticationException(String message) {
        super(BAD_REQUEST, message);
    }
}
