package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class InvalidLoginException extends CustomException {

    public InvalidLoginException() {
        super(ErrorCode.INVALID_LOGIN);
    }
}
