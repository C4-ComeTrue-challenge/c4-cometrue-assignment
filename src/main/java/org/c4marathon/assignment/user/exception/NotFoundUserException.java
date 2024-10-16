package org.c4marathon.assignment.user.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class NotFoundUserException extends ApplicationException {
	public NotFoundUserException() {
		super(NOT_FOUND_USER_ERROR);
	}
}
