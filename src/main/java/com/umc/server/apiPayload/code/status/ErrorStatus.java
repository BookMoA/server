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

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4002", false, "존재하지 않는 회원입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER4001", false, "이미 존재하는 회원입니다."),
    KAKAO_SIGN_IN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER5000", false, "카카오 로그인 에러 입니다."),

    BOOKLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "BOOKLIST4001", false, "책리스트가 없습니다."),
    BOOKLIST_INVALID_STATUS(HttpStatus.BAD_REQUEST, "BOOKLIST4002", false, "STATUS형식이 올바르지 않습니다."),
    BOOKLIST_BOOK_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "BOOKLIST4003", false, "이미 추가된 책입니다."),
    BOOKLIST_BOOK_NO_EXISTS(HttpStatus.NOT_FOUND, "BOOKLIST4004", false, "리스트에 없는 책입니다."),

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK4001", false, "없는 책입니다."),

    INVALID_PAGE(HttpStatus.NOT_FOUND, "PAGE4001", false, "잘못된 페이지입니다.");

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
