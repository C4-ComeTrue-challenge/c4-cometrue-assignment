package org.c4marathon.assignment.global.exception;

import lombok.Getter;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public AuthException(ExceptionCode exceptionCode) {
        this.httpStatus = exceptionCode.getHttpStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}