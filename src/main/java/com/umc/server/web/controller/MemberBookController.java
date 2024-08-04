package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.MemberBookConverter;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.service.MemberBookService.MemberBookService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class MemberBookController {

    private final MemberBookService memberBookService;

    @Operation(summary = "멤버 책 생성 API", description = "멤버 책을 생성하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 멤버 책 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "memberId", description = "멤버 아이디, path variable 입니다.")
    @PostMapping("{memberId}/memberBooks")
    public ApiResponse<MemberBookResponseDTO.CreateMemberBookResultDTO> createMemberBook(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid MemberBookRequestDTO.CreateMemberBookDTO createMemberBookDTO) {
        MemberBook memberBook = memberBookService.createMemberBook(memberId, createMemberBookDTO);
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
    @Parameters({
        @Parameter(name = "memberId", description = "멤버 아이디, path variable 입니다."),
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")
    })
    @GetMapping("{memberId}/memberBooks/{memberBookId}")
    public ApiResponse<MemberBookResponseDTO.MemberBookPreviewDTO> readMemberBook(
            @PathVariable(name = "memberId") Long memberId,
            @PathVariable(name = "memberBookId") Long memberBookId) {
        MemberBook memberBook = memberBookService.readMemberBook(memberId, memberBookId);
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
    @Parameters({
        @Parameter(name = "memberId", description = "멤버 아이디, path variable 입니다."),
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")
    })
    @PatchMapping("{memberId}/memberBooks/{memberBookId}")
    public ApiResponse<MemberBookResponseDTO.MemberBookPreviewDTO> updateMemberBook(
            @PathVariable(name = "memberId") Long memberId,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @RequestBody @Valid MemberBookRequestDTO.UpdateMemberBookDTO updateMemberBookDTO) {
        MemberBook memberBook =
                memberBookService.updateMemberBook(memberId, memberBookId, updateMemberBookDTO);
        return ApiResponse.onSuccess(MemberBookConverter.toMemberBookPreviewDTO(memberBook));
    }

    @Operation(summary = "특정 멤버 책 삭제 API", description = "독서 메모 탭에서 특정 멤버 책을 삭제하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 멤버 책 수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "MEMBER_BOOK4001",
                description = "멤버 책이 아닙니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "memberId", description = "멤버 아이디, path variable 입니다."),
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")
    })
    @DeleteMapping("{memberId}/memberBooks/{memberBookId}")
    public ApiResponse<String> deleteMemberBook(
            @PathVariable(name = "memberId") Long memberId,
            @PathVariable(name = "memberBookId") Long memberBookId) {
        memberBookService.deleteMemberBook(memberId, memberBookId);
        return ApiResponse.onSuccess("독서 메모에서 해당 멤버 책을 삭제하였습니다.");
    }
}
