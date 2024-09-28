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
    MEMBER_NOT_FOUND(UNAUTHORIZED, 1001, "해당하는 계정을 찾을 수 없습니다."),
    REQUEST_LOGIN(UNAUTHORIZED, 1002, "다시 로그인해주세요."),

    // 3000번 대(계좌 관련) 코드
    BALANCE_CANNOT_NEGATIVE(BAD_REQUEST, 3001, "계좌의 금액은 0원 미만이 될 수 없습니다."),
    ACCOUNT_NOT_FOUND(NOT_FOUND, 3002, "해당 계좌를 찾을 수 없습니다."),
    ACCOUNT_BALANCE_NOT_ENOUGH(BAD_REQUEST, 3003, "송금을 하는 계좌의 잔고가 부족합니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
