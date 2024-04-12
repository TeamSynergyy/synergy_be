package com.seoultech.synergybe.domain.user.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;

public class UserBadRequestException extends BadRequestException {
    public UserBadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
