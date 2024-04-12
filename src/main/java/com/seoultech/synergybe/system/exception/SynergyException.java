package com.seoultech.synergybe.system.exception;

public abstract class SynergyException extends RuntimeException {
    private final ErrorCode errorCode;

    protected SynergyException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.getValue();
    }
}
