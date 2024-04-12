package com.seoultech.synergybe.domain.user.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
