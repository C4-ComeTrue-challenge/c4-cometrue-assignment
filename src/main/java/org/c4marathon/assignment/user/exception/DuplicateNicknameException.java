package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class DuplicateNicknameException extends CustomException {
    public DuplicateNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
