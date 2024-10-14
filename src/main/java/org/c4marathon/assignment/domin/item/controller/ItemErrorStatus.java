package org.c4marathon.assignment.domin.item.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.c4marathon.assignment.global.payload.BaseStatus;
import org.c4marathon.assignment.global.payload.ReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorStatus implements BaseStatus {

    INVALID_REGISTER(HttpStatus.BAD_REQUEST, "Item_4001", "상품을 등록할 권한이 없습니다"),
    ITEM_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "Item_4031", "존재하지 않는 상품 정보입니다"),;


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
