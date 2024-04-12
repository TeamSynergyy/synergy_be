package com.seoultech.synergybe.system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
