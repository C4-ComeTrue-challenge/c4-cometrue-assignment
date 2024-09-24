package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
