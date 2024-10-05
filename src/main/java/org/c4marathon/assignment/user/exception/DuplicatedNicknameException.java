package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.ApplicationException;

import static org.c4marathon.assignment.global.exception.ErrorCode.NICKNAME_DUPLICATED_ERROR;

public class DuplicatedNicknameException extends ApplicationException {
    public DuplicatedNicknameException() {
        super(NICKNAME_DUPLICATED_ERROR);
    }
}
