package org.c4marathon.assignment.domin.user.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.c4marathon.assignment.global.payload.BaseStatus;
import org.c4marathon.assignment.global.payload.ReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorStatus implements BaseStatus {

    EXIST_EMAIL(HttpStatus.CONFLICT, "User_4001", "이미 존재하는 이메일입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "User_4002", "비밀번호가 일치하지 않습니다"),

    USER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "User_4040", "존재하지 않는 회원 정보입니다"),

    CONCURRENCY_FAILURE(HttpStatus.CONFLICT, "User_4090", "동시성 문제로 인해 요청을 처리할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
