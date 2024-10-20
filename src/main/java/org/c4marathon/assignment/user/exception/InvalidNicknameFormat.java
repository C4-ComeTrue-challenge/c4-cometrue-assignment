package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class InvalidNicknameFormat extends CustomException {

    public InvalidNicknameFormat() {
        super(ErrorCode.INVALID_NICKNAME);
    }
}
