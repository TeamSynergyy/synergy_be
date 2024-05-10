package com.seoultech.synergybe.domain.library.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class LibraryBadRequestException extends BadRequestException {
    public LibraryBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
