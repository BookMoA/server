package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.BookList;
import com.umc.server.service.BookListService.BookListCommandService;
import com.umc.server.service.BookListService.BookListQueryService;
import com.umc.server.web.dto.BookListRequestDTO;
import com.umc.server.web.dto.BookListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library/list")
public class BookListRestController {

    private final BookListCommandService bookListCommandService;
    private final BookListQueryService bookListQueryService;

    @Operation(
            summary = "책리스트 추가 API",
            description = "책리스트를 추가하는 API입니다. status값에는 PUBLIC이나 PRIVATE로 입력해주세요.")
    @PostMapping("/add")
    public ApiResponse<BookListResponseDTO.AddBookListResultDTO> addBookList(
            @RequestBody @Valid BookListRequestDTO.AddBookListDTO request) {
        BookList bookList = bookListCommandService.addBookList(request);
        return ApiResponse.onSuccess(BookListConverter.toAddBookListResultDTO(bookList));
    }

    @Operation(summary = "특정 책리스트 조회 API", description = "특정 책리스트의 정보를 조회하는 API입니다.")
    @GetMapping("/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.BookListPreviewDTO> getBookList(
            @PathVariable(name = "bookListId") Long bookListId) {
        Optional<BookList> bookList = bookListQueryService.getBookList(bookListId);
        return ApiResponse.onSuccess(BookListConverter.toBookListPreviewDTO(bookList));
    }

    @Operation(summary = "특정 책리스트 수정 API", description = "특정 책리스트의 정보를 수정하는 API입니다.")
    @PatchMapping("/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListRequestDTO.UpdateBookListDTO> updateBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestBody @Valid BookListRequestDTO.UpdateBookListDTO request) {
        BookList bookList = bookListCommandService.updateBookList(bookListId, request);
        return ApiResponse.onSuccess(BookListConverter.toUpdateBookListDTO(bookList));
    }
}
