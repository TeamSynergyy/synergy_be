package com.seoultech.synergybe.domain.comment.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class CommentBadRequestException extends BadRequestException {
    public CommentBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
