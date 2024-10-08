package org.c4marathon.assignment.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.c4marathon.assignment.global.payload.BaseStatus;
import org.c4marathon.assignment.global.payload.ReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseStatus code;

    public ReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
