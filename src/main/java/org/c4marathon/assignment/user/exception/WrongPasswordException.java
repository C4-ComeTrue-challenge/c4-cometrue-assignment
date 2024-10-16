package org.c4marathon.assignment.user.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class WrongPasswordException extends ApplicationException {
	public WrongPasswordException() {
		super(WRONG_PASSWORD_ERROR);
	}
}
