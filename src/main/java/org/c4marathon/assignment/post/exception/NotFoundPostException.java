package org.c4marathon.assignment.post.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class NotFoundPostException extends CustomException {
    public NotFoundPostException() {
        super(ErrorCode.NOT_FOUND_POST);
    }
}
