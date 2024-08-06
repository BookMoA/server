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

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4004", false, "존재하지 않는 회원입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER4001", false, "이미 존재하는 회원입니다."),
    KAKAO_SIGN_IN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER5000", false, "카카오 로그인 에러 입니다."),
    EXPIRED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "MEMBER4010", false, "엑세스 토큰이 만료되었습니다. 재발급해주세요."),
    INVALID_TOKEN_ERROR(HttpStatus.FORBIDDEN, "MEMBER4030", false, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),

    // 책리스트 관련 에러
    BOOKLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "BOOKLIST4001", false, "책리스트가 없습니다."),
    BOOKLIST_INVALID_STATUS(HttpStatus.BAD_REQUEST, "BOOKLIST4002", false, "STATUS형식이 올바르지 않습니다."),
    BOOKLIST_BOOK_ALREADY_EXISTS(HttpStatus.CONFLICT, "BOOKLIST4003", false, "이미 추가된 책입니다."),
    BOOKLIST_BOOK_NO_EXISTS(HttpStatus.NOT_FOUND, "BOOKLIST4004", false, "리스트에 없는 책입니다."),
    BOOKLIST_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "BOOKLIST4005", false, "이미 추가된 리스트입니다."),

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK4001", false, "없는 책입니다."),

    INVALID_PAGE(HttpStatus.NOT_FOUND, "PAGE4001", false, "잘못된 페이지입니다."),

    SEARCH_BOOKLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH4001", false, "없는 책리스트입니다."),
    SEARCH_BOOKMEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH4002", false, "없는 메모입니다."),
    SEARCH_INVALID_SORT(HttpStatus.NOT_FOUND, "SEARCH4003", false, "SORTBY형식이 올바르지 않습니다."),
    // 멤버 책 관련 에러
    MEMBER_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_BOOK4001", false, "멤버 책이 아닙니다."),

    // 검색&조회
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4001", false, "유효하지 않은 분류입니다."),
    PAGE_TOO_SMALL(HttpStatus.BAD_REQUEST, "PAGE4001", false, "유효하지 않은 페이지 번호입니다."),

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
