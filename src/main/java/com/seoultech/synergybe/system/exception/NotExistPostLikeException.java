package com.seoultech.synergybe.system.exception;

public class NotExistPostLikeException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 좋아요입니다";

    public NotExistPostLikeException() {
        super(MESSAGE);
    }
}
