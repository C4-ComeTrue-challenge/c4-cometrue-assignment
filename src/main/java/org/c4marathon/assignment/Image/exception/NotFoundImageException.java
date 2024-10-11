package org.c4marathon.assignment.Image.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class NotFoundImageException extends CustomException {

    public NotFoundImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
