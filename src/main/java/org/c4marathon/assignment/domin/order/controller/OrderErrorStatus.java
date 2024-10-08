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

    REJECT_PAYMENT(HttpStatus.UNAUTHORIZED, "Order_4030", "캐시가 부족합니다"),
    INVALID_ORDER_STATUS(HttpStatus.UNAUTHORIZED, "Order_4031", "이미 배송한 상품입니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "Order_4031", "환불할 권한이 없습니다"),

    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "Order_4040", "주문을 찾을 수 없습니다"),;


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
