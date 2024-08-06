package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.service.BookListService.BookListService;
import com.umc.server.web.dto.request.BookListRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library/list")
public class BookListRestController {
    private final BookListService bookListService;

    @Operation(
            summary = "책리스트 추가 API",
            description = "책리스트를 추가하는 API입니다. status값에는 PUBLIC이나 PRIVATE로 입력해주세요.")
    @PostMapping("/add")
    public ApiResponse<BookListResponseDTO.AddBookListResultDTO> addBookList(
            @RequestBody @Valid BookListRequestDTO.AddBookListDTO request,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        BookList bookList = bookListService.addBookList(request, signInmember);
        return ApiResponse.onSuccess(BookListConverter.toAddBookListResultDTO(bookList));
    }

    @Operation(summary = "특정 책리스트 조회 API", description = "특정 책리스트의 정보를 조회하는 API입니다.")
    @GetMapping("/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.BookListPreviewDTO> getBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        Long memberId = signInmember.getId();
        Optional<BookList> bookList = bookListService.getBookList(bookListId);
        return ApiResponse.onSuccess(BookListConverter.toBookListPreviewDTO(bookList, memberId));
    }

    @Operation(
            summary = "특정 책리스트 수정 API",
            description = "특정 책리스트의 정보를 수정하는 API입니다. 책리스트의 책값을 모두 적어줘야 수정이 반영됩니다.")
    @PatchMapping("/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListRequestDTO.UpdateBookListDTO> updateBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestBody @Valid BookListRequestDTO.UpdateBookListDTO request) {
        BookList bookList = bookListService.updateBookList(bookListId, request);
        return ApiResponse.onSuccess(BookListConverter.toUpdateBookListDTO(bookList));
    }

    @Operation(summary = "책리스트 삭제 API", description = "책리스트를 삭제하는 API입니다.")
    @DeleteMapping("/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<?> deleteBookList(@PathVariable(name = "bookListId") Long bookListId) {
        bookListService.deleteBookList(bookListId);
        return ApiResponse.onSuccess("삭제에 성공하였습니다!");
    }

    @Operation(
            summary = "보관함 책리스트 조회 API",
            description = "보관함 책리스트를 조회하는 API입니다. (내가 작성한 리스트 + 타 유저가 만든 리스트 저장한것 모두 출력)")
    @GetMapping("")
    public ApiResponse<List<BookListResponseDTO.LibraryBookListDTO>> getLibraryBookList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        List<BookListResponseDTO.LibraryBookListDTO> bookListDTOs =
                bookListService.getLibraryBookList(page, signInmember);
        return ApiResponse.onSuccess(bookListDTOs);
    }

    @Operation(summary = "책리스트의 책 추가 API", description = "책리스트에서 책을 추가하는 API입니다.")
    @PostMapping("/book/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.AddBookInBookListResultDTO> addBookInBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestBody @Valid BookListRequestDTO.AddBookInBookListDTO request) {
        // 책리스트에 책을 추가
        List<Long> addBookIds = bookListService.addBookInBookList(bookListId, request);
        return ApiResponse.onSuccess(BookListConverter.addBookInBookListResultDTO(addBookIds));
    }

    @Operation(summary = "책리스트의 책 삭제 API", description = "책리스트에서 책을 삭제하는 API입니다.")
    @DeleteMapping("book/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<?> deleteBookInBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestBody @Valid BookListRequestDTO.DeleteBookInBookListDTO request) {
        bookListService.deleteBookInBookList(bookListId, request);
        return ApiResponse.onSuccess("책 리스트의 책을 삭제에 성공하였습니다!");
    }

    @Operation(summary = "책리스트 좋아요 추가 API", description = "책리스트에서 좋아요를 추가하는 API입니다.")
    @PostMapping("/likes/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<?> addLikeToBookList(
            @PathVariable Long bookListId,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        String response = bookListService.toggleLike(bookListId, signInmember);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "인기 책리스트 조회 API", description = "인기 책리스트를 조회하는 API입니다.")
    @GetMapping("/top")
    public ApiResponse<BookListResponseDTO.TopBookListAndTimeDTO> getTopBookList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        BookListResponseDTO.TopBookListAndTimeDTO topBookList =
                bookListService.getTopBookList(page, signInmember);
        return ApiResponse.onSuccess(topBookList);
    }

    @Operation(summary = "타사용자 책리스트 추가 API", description = "타사용자 책리스트를 보관함에 추가하는 API입니다.")
    @PostMapping("/{bookListId}/another")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.AddaAnotherBookListResultDTO>
            addAnotherBookListToLibrary(
                    @PathVariable Long bookListId,
                    @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        BookListResponseDTO.AddaAnotherBookListResultDTO response =
                bookListService.anotherToLibrary(bookListId, signInmember);
        return ApiResponse.onSuccess(response);
    }
}
