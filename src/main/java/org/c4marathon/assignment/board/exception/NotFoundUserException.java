package org.c4marathon.assignment.board.exception;

import org.c4marathon.assignment.global.exception.ApplicationException;

import static org.c4marathon.assignment.global.exception.ErrorCode.NOT_FOUND_USER_ERROR;

public class NotFoundUserException extends ApplicationException {
    public NotFoundUserException() {
        super(NOT_FOUND_USER_ERROR);
    }
}
