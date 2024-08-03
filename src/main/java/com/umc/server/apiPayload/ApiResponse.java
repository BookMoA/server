package com.umc.server.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.umc.server.apiPayload.code.BaseCode;
import com.umc.server.apiPayload.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"status", "code", "result", "description", "data"})
public class ApiResponse<T> {

    //    @JsonProperty("isSuccess")

    private final String status;
    private final String code;
    private final Boolean result;
    private final String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 성공한 경우 응답 생성
    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(
                "Ok", SuccessStatus._OK.getCode(), true, SuccessStatus._OK.getDescription(), data);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T data) {
        return new ApiResponse<>(
                "Ok",
                code.getReasonHttpStatus().getCode(),
                true,
                code.getReasonHttpStatus().getDescription(),
                data);
    }

    // 실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String description, T data) {
        return new ApiResponse<>("Bad Request", code, false, description, data);
    }
}
