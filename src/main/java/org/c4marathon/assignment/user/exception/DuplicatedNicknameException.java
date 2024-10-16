package org.c4marathon.assignment.user.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class DuplicatedNicknameException extends ApplicationException {
	public DuplicatedNicknameException() {
		super(NICKNAME_DUPLICATED_ERROR);
	}
}
