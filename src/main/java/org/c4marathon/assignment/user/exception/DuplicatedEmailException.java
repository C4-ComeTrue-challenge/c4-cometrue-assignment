package org.c4marathon.assignment.user.exception;

import org.c4marathon.assignment.global.exception.ApplicationException;

import static org.c4marathon.assignment.global.exception.ErrorCode.EMAIL_DUPLICATED_ERROR;

public class DuplicatedEmailException extends ApplicationException {

    public DuplicatedEmailException() {
        super(EMAIL_DUPLICATED_ERROR);
    }
}
