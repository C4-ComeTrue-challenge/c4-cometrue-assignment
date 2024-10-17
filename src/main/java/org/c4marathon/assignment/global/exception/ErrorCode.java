package org.c4marathon.assignment.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	BAD_REQUEST("400", HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
	METHOD_NOT_ALLOWED("405", HttpStatus.METHOD_NOT_ALLOWED, "클라이언트가 사용한 HTTP 메서드가 리소스에서 허용되지 않습니다."),
	INTERNAL_SERVER_ERROR("500", HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 요청을 처리하는 동안 오류가 발생했습니다."),

	NICKNAME_DUPLICATED_ERROR("AU0001", HttpStatus.BAD_REQUEST, "닉네임이 중복됩니다."),
	EMAIL_DUPLICATED_ERROR("AU0002", HttpStatus.BAD_REQUEST, "이메일이 중복됩니다"),
	NOT_FOUND_USER_ERROR("AU0003", HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다"),
	WRONG_PASSWORD_ERROR("AU0004", HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),

	NOT_FOUND_BOARD_ERROR("B0001", HttpStatus.NOT_FOUND, "해당하는 게시글이 없습니다."),
	NOT_FOUND_IMG_ERROR("B0002", HttpStatus.NOT_FOUND, "유효하지 않는 img url이 있습니다");

	private final String errorCode;
	private final HttpStatus httpStatus;
	private final String message;

}
