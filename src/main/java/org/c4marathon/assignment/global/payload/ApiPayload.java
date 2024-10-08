package org.c4marathon.assignment.global.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiPayload<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 성공
    public static <T> ApiPayload<T> onSuccess(BaseStatus status, T data) {
        return new ApiPayload<>(true, status.getReasonHttpStatus().getCode(), status.getReasonHttpStatus().getMessage(), data);
    }

    // 실패
    public static <T> ApiPayload<T> onFailure(BaseStatus status, T data) {
        return new ApiPayload<>(false, status.getReasonHttpStatus().getCode(), status.getReasonHttpStatus().getMessage(), data);
    }

    public static <T> ApiPayload<T> onFailure(String code, String message, T data) {
        return new ApiPayload<>(false, code, message, data);
    }
}

