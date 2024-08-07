package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.MemberBookConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.service.MemberBookService.MemberBookService;
import com.umc.server.validation.annotation.CheckPage;
import com.umc.server.web.dto.request.MemberBookRequestDTO;
import com.umc.server.web.dto.response.MemberBookResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memberBooks/")
public class MemberBookRestController {

    private final MemberBookService memberBookService;

    @Operation(summary = "멤버 책 생성 API", description = "멤버가 멤버 책을 생성하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 멤버 책 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping
    public ApiResponse<MemberBookResponseDTO.CreateMemberBookResultDTO> createMemberBook(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid MemberBookRequestDTO.CreateMemberBookDTO createMemberBookDTO) {
        MemberBook memberBook =
                memberBookService.createMemberBook(signInmember, createMemberBookDTO);
        return ApiResponse.onSuccess(MemberBookConverter.toCreateMemberBookResult(memberBook));
    }

    @Operation(summary = "특정 멤버 책 조회 API", description = "특정 멤버 책을 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 멤버 책 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "MEMBER_BOOK4001",
                description = "멤버 책이 아닙니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({@Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")})
    @GetMapping("{memberBookId}")
    public ApiResponse<MemberBookResponseDTO.MemberBookPreviewDTO> readMemberBook(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId) {
        MemberBook memberBook =
                memberBookService.readMemberBook(signInmember.getId(), memberBookId);
        return ApiResponse.onSuccess(MemberBookConverter.toMemberBookPreviewDTO(memberBook));
    }

    @Operation(summary = "특정 멤버 책 수정 API", description = "특정 멤버 책을 수정하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 멤버 책 수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "MEMBER_BOOK4001",
                description = "멤버 책이 아닙니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({@Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")})
    @PatchMapping("{memberBookId}")
    public ApiResponse<MemberBookResponseDTO.MemberBookPreviewDTO> updateMemberBook(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @RequestBody @Valid MemberBookRequestDTO.UpdateMemberBookDTO updateMemberBookDTO) {
        MemberBook memberBook =
                memberBookService.updateMemberBook(
                        signInmember.getId(), memberBookId, updateMemberBookDTO);
        return ApiResponse.onSuccess(MemberBookConverter.toMemberBookPreviewDTO(memberBook));
    }

    @Operation(summary = "특정 멤버 책 삭제 API", description = "독서 메모 탭에서 특정 멤버 책을 삭제하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 멤버 책 삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "MEMBER_BOOK4001",
                description = "멤버 책이 아닙니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "memberId", description = "멤버 아이디, path variable 입니다."),
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")
    })
    @DeleteMapping("{memberBookId}")
    public ApiResponse<String> deleteMemberBook(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId) {
        memberBookService.deleteMemberBook(signInmember.getId(), memberBookId);
        return ApiResponse.onSuccess("독서 메모에서 해당 멤버 책을 삭제하였습니다.");
    }

    @Operation(summary = "독서 메모 멤버 책 조회 API", description = "독서 메모가 있는 멤버 책 하나를 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 멤버 책 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({@Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")})
    @GetMapping("bookMemos/{memberBookId}")
    public ApiResponse<MemberBookResponseDTO.MemberBookPreviewDTO> readMemberBookByBookMemo(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId) {
        MemberBook memberBook =
                memberBookService.readMemberBookByBookMemo(signInmember, memberBookId);
        return ApiResponse.onSuccess(MemberBookConverter.toMemberBookPreviewDTO(memberBook));
    }

    @Operation(summary = "독서 메모 멤버 책 전체 조회 API", description = "독서 메모가 있는 멤버 책 전체를 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 멤버 책 전체 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("bookMemos")
    public ApiResponse<MemberBookResponseDTO.MemberBookPreviewListDTO> readMemberBookListByBookMemo(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @CheckPage @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<MemberBook> memoMemberBookPage =
                memberBookService.readMemberBookListByBookMemo(signInmember, page);
        return ApiResponse.onSuccess(
                MemberBookConverter.toMemberBookPreviewListDTO(memoMemberBookPage));
    }
}
