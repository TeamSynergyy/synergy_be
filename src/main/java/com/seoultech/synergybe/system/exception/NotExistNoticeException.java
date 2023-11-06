package com.seoultech.synergybe.system.exception;

public class NotExistNoticeException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 공지입니다";

    public NotExistNoticeException() {
        super(MESSAGE);
    }
}
