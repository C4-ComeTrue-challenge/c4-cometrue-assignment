package org.c4marathon.assignment.global.payload;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ReasonDTO {

    private HttpStatus httpStatus;

    private final Boolean isSuccess;
    private final String code;
    private final String message;
}
