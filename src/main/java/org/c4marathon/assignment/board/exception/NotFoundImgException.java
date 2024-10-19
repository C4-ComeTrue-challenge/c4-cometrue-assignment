package org.c4marathon.assignment.board.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class NotFoundImgException extends ApplicationException {
	public NotFoundImgException() {
		super(NOT_FOUND_IMG_ERROR);
	}
}
