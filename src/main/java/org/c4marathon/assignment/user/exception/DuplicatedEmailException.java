package org.c4marathon.assignment.user.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class DuplicatedEmailException extends ApplicationException {

	public DuplicatedEmailException() {
		super(EMAIL_DUPLICATED_ERROR);
	}
}
