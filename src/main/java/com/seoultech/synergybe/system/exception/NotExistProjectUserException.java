package com.seoultech.synergybe.system.exception;

public class NotExistProjectUserException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 프로젝트 유저입니다";

    public NotExistProjectUserException() {
        super(MESSAGE);
    }

}
