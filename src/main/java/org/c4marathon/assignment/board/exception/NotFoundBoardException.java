package org.c4marathon.assignment.board.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class NotFoundBoardException extends CustomException {
    public NotFoundBoardException() {
        super(ErrorCode.NOT_FOUND_BOARD);
    }
}
