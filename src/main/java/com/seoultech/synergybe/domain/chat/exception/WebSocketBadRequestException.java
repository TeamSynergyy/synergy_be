package com.seoultech.synergybe.domain.chat.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;

public class WebSocketBadRequestException extends BadRequestException {
    public WebSocketBadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
