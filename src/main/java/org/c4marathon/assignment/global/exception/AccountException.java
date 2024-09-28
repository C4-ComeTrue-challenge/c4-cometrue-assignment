package org.c4marathon.assignment.global.exception;

import lombok.Getter;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public class AccountException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public AccountException(ExceptionCode exceptionCode) {
        this.httpStatus = exceptionCode.getHttpStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
