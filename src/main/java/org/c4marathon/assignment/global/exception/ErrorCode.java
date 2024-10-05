package org.c4marathon.assignment.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //User
    DUPLICATE_NICKNAME(400, "중복된 닉네임 입니다."),
    DUPLICATE_EMAIL(400, "이미 가입한 이메일 입니다."),
    NOT_FOUND_USER(404, "조회된 유저가 없습니다."),
    INVALID_LOGIN(400, "잘못된 자격 증명입니다."),

    //Board
    DUPLICATE_NAME(400, "중복된 게시판 이름 입니다."),
    NOT_FOUND_BOARD(404, "조회된 게시판이 없습니다.");

    private final int status;
    private final String message;


}
