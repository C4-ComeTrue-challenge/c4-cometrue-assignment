package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.ApplicationException;

import static org.c4marathon.assignment.global.exception.ErrorCode.WRONG_PASSWORD_ERROR;

public class WrongPasswordException extends ApplicationException {
    public WrongPasswordException() {
        super(WRONG_PASSWORD_ERROR);
    }
}
