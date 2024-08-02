package com.umc.server.apiPayload.code.status;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", false, "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", false, "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", false, "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", false, "금지된 요청입니다."),

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK4001", false, "없는 책입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final boolean result;
    private final String description;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .status("Bad Request")
                .code(code)
                .result(false)
                .description(description)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .status("Bad Request")
                .code(code)
                .result(false)
                .description(description)
                .httpStatus(httpStatus)
                .build();
    }
}
