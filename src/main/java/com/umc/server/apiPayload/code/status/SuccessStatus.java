package com.umc.server.apiPayload.code.status;

import com.umc.server.apiPayload.code.BaseCode;
import com.umc.server.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", true, "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", true, "요청 성공 및 리소스 생성됨");

    private final HttpStatus httpStatus;
    private final String code;
    private final boolean result;
    private final String description;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .status("Ok")
                .code(code)
                .result(true)
                .description(description)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .status("Ok")
                .code(code)
                .result(true)
                .description(description)
                .httpStatus(httpStatus)
                .build();
    }
}
