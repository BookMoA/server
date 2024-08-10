package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookMemoConverter;
import com.umc.server.domain.BookMemo;
import com.umc.server.domain.Member;
import com.umc.server.service.BookMemoService.BookMemoService;
import com.umc.server.validation.annotation.CheckPage;
import com.umc.server.web.dto.request.BookMemoRequestDTO;
import com.umc.server.web.dto.response.BookMemoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memberBooks/")
public class BookMemoRestController {
    private final BookMemoService bookMemoService;

    @Operation(summary = "독서 메모 생성 API", description = "멤버가 독서 메모를 생성하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({@Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다.")})
    @PostMapping("{memberBookId}/bookMemos")
    public ApiResponse<BookMemoResponseDTO.CreateBookMemoResultDTO> createBookMemo(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @RequestBody BookMemoRequestDTO.CreateBookMemoDTO createBookMemoDTO) {
        BookMemo bookMemo =
                bookMemoService.createBookMemo(signInmember, memberBookId, createBookMemoDTO);
        return ApiResponse.onSuccess(BookMemoConverter.toCreateBookMemoResultDTO(bookMemo));
    }

    @Operation(summary = "특정 독서 메모 조회 API", description = "멤버가 특정 독서 메모를 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다."),
        @Parameter(name = "bookMemoId", description = "독서 메모의 아이디, path variable 입니다.")
    })
    @GetMapping("{memberBookId}/bookMemos/{bookMemoId}")
    public ApiResponse<BookMemoResponseDTO.BookMemoPreviewDTO> readBookMemo(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @PathVariable(name = "bookMemoId") Long bookMemoId) {
        BookMemo bookMemo = bookMemoService.readBookMemo(signInmember, memberBookId, bookMemoId);
        return ApiResponse.onSuccess(BookMemoConverter.toBookMemoPreviewDTO(bookMemo));
    }

    @Operation(summary = "독서 메모 전체 조회 API", description = "멤버가 특정 멤버 책의 독서 메모를 전체 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 전체 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다."),
        @Parameter(name = "page", description = "페이지 번호(default는 1, 메모 10개씩 한 페이지)입니다.")
    })
    @GetMapping("{memberBookId}/bookMemos")
    public ApiResponse<BookMemoResponseDTO.BookMemoPreviewListDTO> readBookMemoList(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @CheckPage @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<BookMemo> bookMemoPage =
                bookMemoService.readBookMemoList(signInmember, memberBookId, page);
        return ApiResponse.onSuccess(BookMemoConverter.toBookMemoPreviewListDTO(bookMemoPage));
    }

    @Operation(summary = "독서 메모 수정 API", description = "멤버가 특정 독서 메모를 수정하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다."),
        @Parameter(name = "bookMemoId", description = "독서 메모의 아이디, path variable 입니다.")
    })
    @PatchMapping("{memberBookId}/bookMemos/{bookMemoId}")
    public ApiResponse<BookMemoResponseDTO.BookMemoPreviewDTO> updateBookMemo(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @PathVariable(name = "bookMemoId") Long bookMemoId,
            @RequestBody BookMemoRequestDTO.UpdateBookMemoDTO updateBookMemoDTO) {
        BookMemo bookMemo =
                bookMemoService.updateBookMemo(
                        signInmember, memberBookId, bookMemoId, updateBookMemoDTO);
        return ApiResponse.onSuccess(BookMemoConverter.toBookMemoPreviewDTO(bookMemo));
    }

    @Operation(summary = "특정 독서 메모 삭제 API", description = "멤버가 특정 독서 메모를 삭제하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 독서 메모 삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "memberBookId", description = "멤버 책의 아이디, path variable 입니다."),
        @Parameter(name = "bookMemoId", description = "독서 메모의 아이디, path variable 입니다.")
    })
    @DeleteMapping("{memberBookId}/bookMemos/{bookMemoId}")
    public ApiResponse<String> deleteBookMemo(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @PathVariable(name = "memberBookId") Long memberBookId,
            @PathVariable(name = "bookMemoId") Long bookMemoId) {
        bookMemoService.deleteBookMemo(signInmember, memberBookId, bookMemoId);
        return ApiResponse.onSuccess("멤버 책에서 해당 독서 메모를 삭제하였습니다.");
    }
}
