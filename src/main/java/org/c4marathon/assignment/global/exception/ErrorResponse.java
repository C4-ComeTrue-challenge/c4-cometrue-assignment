package org.c4marathon.assignment.global.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse<T> {

	private LocalDateTime timeStamp;
	private String errorCode;
	private String message;

	public ErrorResponse(ErrorCode errorCode, String message) {
		this.timeStamp = LocalDateTime.now().withNano(0);
		this.errorCode = errorCode.getErrorCode();
		this.message = message;
	}

	public ErrorResponse(ErrorCode errorCode) {
		this.timeStamp = LocalDateTime.now().withNano(0);
		this.errorCode = errorCode.getErrorCode();
		this.message = errorCode.getMessage();
	}

}
