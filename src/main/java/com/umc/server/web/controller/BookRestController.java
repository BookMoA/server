package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookConverter;
import com.umc.server.domain.Book;
import com.umc.server.service.BookService.BookService;
import com.umc.server.web.dto.BookRequestDTO;
import com.umc.server.web.dto.BookResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class BookRestController {

    private final BookService bookService;

    @Operation(summary = "책 생성 API", description = "책 생성하는 API입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "OK, 책 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "BOOK4001",
                description = "책을 찾을 수 없습니다.",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("books/")
    public ApiResponse<BookResponseDTO.CreateBookResultDTO> createBook(
            @RequestBody BookRequestDTO.CreateBookDTO createBookDTO) {
        Book book = bookService.createBook(createBookDTO);
        return ApiResponse.onSuccess(BookConverter.toCreateBookResultDTO(book));
    }
}
