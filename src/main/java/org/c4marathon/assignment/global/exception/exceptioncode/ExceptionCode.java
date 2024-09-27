package org.c4marathon.assignment.global.exception.exceptioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    // 1000번 대(기본, 계정 관련) 코드
    INVALID_REQUEST(BAD_REQUEST, 1000, "올바르지 않은 요청입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
