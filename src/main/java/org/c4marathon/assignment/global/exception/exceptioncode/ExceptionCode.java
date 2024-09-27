package org.c4marathon.assignment.global.exception.exceptioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    // 1000번 대(기본, 계정 관련) 코드
    INVALID_REQUEST(BAD_REQUEST, 1000, "올바르지 않은 요청입니다."),
    ACCOUNT_NOT_FOUND(UNAUTHORIZED, 1001, "해당하는 계정을 찾을 수 없습니다."),
    REQUEST_LOGIN(UNAUTHORIZED, 1002, "다시 로그인해주세요.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
