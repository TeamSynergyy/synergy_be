package com.seoultech.synergybe.domain.post.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class PostBadRequestException extends BadRequestException {
    public PostBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
