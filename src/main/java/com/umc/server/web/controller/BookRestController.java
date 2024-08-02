package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookConverter;
import com.umc.server.domain.Book;
import com.umc.server.service.BookService.BookService;
import com.umc.server.web.dto.BookRequestDTO;
import com.umc.server.web.dto.BookResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class BookRestController {

    private final BookService bookService;

    @Operation(summary = "책 생성 API", description = "책을 생성하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 책 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON400",
                description = "잘못된 요청입니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("books/")
    public ApiResponse<BookResponseDTO.CreateBookResultDTO> createBook(
            @RequestBody BookRequestDTO.CreateBookDTO createBookDTO) {
        Book book = bookService.createBook(createBookDTO);
        return ApiResponse.onSuccess(BookConverter.toCreateBookResultDTO(book));
    }

    @Operation(summary = "특정 책 조회 API", description = "특정 책을 조회하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 책 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "BOOK4001",
                description = "책을 찾을 수 없습니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "bookId", description = "책의 아이디, path variable 입니다.")
    @GetMapping("books/{bookId}")
    public ApiResponse<BookResponseDTO.BookPreviewDTO> readBook(
            @PathVariable(name = "bookId") Long bookId) {
        Book book = bookService.readBook(bookId);
        return ApiResponse.onSuccess(BookConverter.toBookPreviewDTO(book));
    }
}
