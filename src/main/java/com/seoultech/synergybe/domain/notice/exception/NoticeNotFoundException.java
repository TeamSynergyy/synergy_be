package com.seoultech.synergybe.domain.notice.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class NoticeNotFoundException extends NotFoundException {
    public NoticeNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
