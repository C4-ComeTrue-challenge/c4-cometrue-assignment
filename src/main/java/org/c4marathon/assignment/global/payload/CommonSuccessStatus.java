package org.c4marathon.assignment.global.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonSuccessStatus implements BaseStatus {

    // 일반적인 응답
    OK(HttpStatus.OK, "COMMON_200", "성공"),
    CREATED(HttpStatus.CREATED, "COMMON_201", "생성됨"),
    ACCEPTED(HttpStatus.ACCEPTED, "COMMON_202", "허용됨"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
