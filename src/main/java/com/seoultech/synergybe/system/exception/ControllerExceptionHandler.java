package com.seoultech.synergybe.system.exception;

import com.seoultech.synergybe.system.exception.dto.NotFoundFailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {
            NotExistApplyException.class,
            NotExistCommentException.class,
            NotExistFollowException.class,
            NotExistNoticeException.class,
            NotExistPostException.class,
            NotExistPostLikeException.class,
            NotExistProjectException.class,
            NotExistProjectLikeException.class,
            NotExistProjectUserException.class,
            NotExistScheduleException.class,
            NotExistUserException.class,
            NotProjectLeaderException.class
    })
    public ResponseEntity<NotFoundFailResponse> notFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(NotFoundFailResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build()
                );
    }
}
