package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class NotFountUserException extends CustomException {
    public NotFountUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}

