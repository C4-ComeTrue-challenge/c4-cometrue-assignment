package org.c4marathon.assignment.board.exception;

import org.c4marathon.assignment.global.exception.ApplicationException;

import static org.c4marathon.assignment.global.exception.ErrorCode.NOT_FOUND_BOARD_ERROR;

public class NotFoundBoardException extends ApplicationException {
    public NotFoundBoardException() {
        super(NOT_FOUND_BOARD_ERROR);
    }
}
