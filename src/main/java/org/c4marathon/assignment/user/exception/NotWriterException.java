package org.c4marathon.assignment.user.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class NotWriterException extends ApplicationException {
	public NotWriterException() {
		super(NOT_WRITER_ERROR);
	}
}
