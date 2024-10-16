package org.c4marathon.assignment.board.exception;

import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.global.exception.ApplicationException;

public class NotFoundBoardException extends ApplicationException {
	public NotFoundBoardException() {
		super(NOT_FOUND_BOARD_ERROR);
	}
}
