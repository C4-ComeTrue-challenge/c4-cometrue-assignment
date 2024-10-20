package org.c4marathon.assignment.Image.exception;

import org.c4marathon.assignment.global.exception.CustomException;
import org.c4marathon.assignment.global.exception.ErrorCode;

public class ImageSaveFailedException extends CustomException {

    public ImageSaveFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
