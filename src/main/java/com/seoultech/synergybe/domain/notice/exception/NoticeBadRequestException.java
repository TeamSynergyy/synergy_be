package com.seoultech.synergybe.domain.notice.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class NoticeBadRequestException extends BadRequestException {
    public NoticeBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
