package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class DuplicateNameException extends CustomException {
    public DuplicateNameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
