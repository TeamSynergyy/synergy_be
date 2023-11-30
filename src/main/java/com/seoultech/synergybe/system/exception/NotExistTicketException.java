package com.seoultech.synergybe.system.exception;

public class NotExistTicketException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 티켓입니다";

    public NotExistTicketException() {
        super(MESSAGE);
    }
}
