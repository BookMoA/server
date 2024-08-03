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

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", false, "사용자가 없습니다."),

    BOOKLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "BOOKLIST4001", false, "책리스트가 없습니다."),
    BOOKLIST_INVALID_STATUS(HttpStatus.BAD_REQUEST, "BOOKLIST4002", false, "STATUS형식이 올바르지 않습니다."),

    // 독서 모임
    CLUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "CLUB4001", false, "독서 모임이 없습니다."),
    CLUB_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CLUB4002", false, "이미 존재하는 독서 모임 이름입니다."),

    // 독서 모임원
    CLUB_NOT_JOINED(HttpStatus.BAD_REQUEST, "CLUBMEMBER4001", false, "가입된 독서 모임이 없는 사용자입니다."),
    CLUB_ALREADY_JOINED(
            HttpStatus.BAD_REQUEST, "CLUBMEMBER4002", false, "이미 가입된 독서 모임이 있는 사용자입니다."),
    CLUB_LEADER_REQUIRED(
            HttpStatus.BAD_REQUEST,
            "CLUBMEMBER4003",
            false,
            "권한이 불충분합니다. 독서 모임 리더의 권한이 필요한 동작입니다."),
    CLUB_MEMBER_REQUIRED(
            HttpStatus.BAD_REQUEST, "CLUBMEMBER4004", false, "권한이 불충분합니다. 가입된 독서 모임이 아닙니다.");

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
