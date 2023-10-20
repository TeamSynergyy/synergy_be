package com.seoultech.synergybe.system.exception;

public class NotExistCommentException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 댓글입니다";

    public NotExistCommentException() {
        super(MESSAGE);
    }
}
