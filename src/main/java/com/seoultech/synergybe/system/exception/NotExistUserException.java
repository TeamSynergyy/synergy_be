package com.seoultech.synergybe.system.exception;

public class NotExistUserException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 유저 입니다";

    public NotExistUserException() {
        super(MESSAGE);
    }
}
