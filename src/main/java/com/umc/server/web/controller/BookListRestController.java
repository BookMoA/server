package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.BookList;
import com.umc.server.service.BookListService.BookListCommandService;
import com.umc.server.service.BookListService.BookListQueryService;
import com.umc.server.web.dto.BookListRequestDTO;
import com.umc.server.web.dto.BookListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
}
