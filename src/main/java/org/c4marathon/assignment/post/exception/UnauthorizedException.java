package org.c4marathon.assignment.post.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
