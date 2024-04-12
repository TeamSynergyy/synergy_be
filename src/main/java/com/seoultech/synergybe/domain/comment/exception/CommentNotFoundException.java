package com.seoultech.synergybe.domain.comment.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
