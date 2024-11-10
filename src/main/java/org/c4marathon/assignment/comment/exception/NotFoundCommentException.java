package org.c4marathon.assignment.comment.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class NotFoundCommentException extends ApplicationException {
	public NotFoundCommentException() {
		super(NOT_FOUND_COMMENT_ERROR);
	}
}
