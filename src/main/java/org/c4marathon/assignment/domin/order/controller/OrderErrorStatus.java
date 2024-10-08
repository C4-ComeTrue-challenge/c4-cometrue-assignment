package org.c4marathon.assignment.domin.order.controller;

import ch.qos.logback.core.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.c4marathon.assignment.global.payload.BaseStatus;
import org.c4marathon.assignment.global.payload.ReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorStatus implements BaseStatus {

    INVALID_CREAT_ORDER(HttpStatus.UNAUTHORIZED, "Order_4001", "상품의 수량이 부족합니다"),
    REJECT_PAYMENT(HttpStatus.UNAUTHORIZED, "Order_4030", "캐시가 부족합니다"),;


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
