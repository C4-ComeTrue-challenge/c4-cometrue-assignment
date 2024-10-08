package org.c4marathon.assignment.global.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorStatus implements BaseStatus {

    // 일반적인 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러, 관리자에게 문의하세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401", "권한 없음"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_404", "찾을 수 없음"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_405", "허용되지 않은 메소드"),
    CONFLICT(HttpStatus.CONFLICT, "COMMON_409", "충돌"),
    CONCURRENCY_FAILURE(HttpStatus.CONFLICT, "COMMON_4091", "동시성 문제로 인해 요청을 처리할 수 없습니다");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
