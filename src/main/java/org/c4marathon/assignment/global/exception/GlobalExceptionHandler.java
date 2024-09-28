package org.c4marathon.assignment.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.c4marathon.assignment.global.exception.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.INVALID_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthException e) {

        return ResponseEntity.status(e.getHttpStatus())
                             .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(
            final SQLIntegrityConstraintViolationException e
    ) {
        ExceptionCode exceptionCode = INVALID_REQUEST;
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                             .body(new ErrorResponse(exceptionCode.getCode(), exceptionCode.getMessage()));
    }
}
