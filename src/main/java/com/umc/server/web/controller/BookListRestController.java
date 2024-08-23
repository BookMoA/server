package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.service.BookListService.BookListService;
import com.umc.server.web.dto.request.BookListRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class BookListRestController {
    private final BookListService bookListService;

    // 책리스트 추가
    @Operation(
            summary = "책리스트 추가 API",
            description = "책리스트를 추가하는 API입니다. status값에는 PUBLIC이나 PRIVATE로 입력해주세요.")
    @PostMapping(
            value = "list/add",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<BookListResponseDTO.AddBookListResultDTO> addBookList(
            @Valid @RequestPart("request") BookListRequestDTO.AddBookListDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember)
            throws IOException {
        BookList bookList = bookListService.addBookList(request, signInmember, img);
        return ApiResponse.onSuccess(BookListConverter.toAddBookListResultDTO(bookList));
    }

    @Operation(summary = "특정 책리스트 조회 API", description = "특정 책리스트의 정보를 조회하는 API입니다.")
    @GetMapping("list/{bookListId}")
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
    @PatchMapping(
            value = "list/{bookListId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.UpdateBookListResultDTO> updateBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestPart("request") BookListRequestDTO.UpdateBookListDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img)
            throws IOException {
        BookList bookList = bookListService.updateBookList(bookListId, request, img);
        return ApiResponse.onSuccess(BookListConverter.toUpdateBookListDTO2(bookList));
    }

    @Operation(summary = "책리스트 삭제 API", description = "책리스트를 삭제하는 API입니다.")
    @DeleteMapping("list")
    public ApiResponse<?> deleteBookList(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestParam List<Long> bookListIds) {
        BookListRequestDTO.DeleteBookListDTO request =
                BookListRequestDTO.DeleteBookListDTO.builder().bookListId(bookListIds).build();
        bookListService.deleteBookList(request, signInmember);
        return ApiResponse.onSuccess("삭제에 성공하였습니다!");
    }

    //
    @Operation(
            summary = "보관함 책리스트 조회 API",
            description = "보관함 책리스트를 조회하는 API입니다. (내가 작성한 리스트 + 타 유저가 만든 리스트 저장한것 모두 출력)")
    @GetMapping("list")
    public ApiResponse<List<BookListResponseDTO.LibraryBookListDTO>> getLibraryBookList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        List<BookListResponseDTO.LibraryBookListDTO> bookListDTOs =
                bookListService.getLibraryBookList(page, signInmember);
        return ApiResponse.onSuccess(bookListDTOs);
    }

    @Operation(summary = "책리스트의 책 추가 API", description = "책리스트에서 책을 추가하는 API입니다.")
    @PostMapping("list/book/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.AddBookInBookListResultDTO> addBookInBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestBody @Valid BookListRequestDTO.AddBookInBookListDTO request) {
        // 책리스트에 책을 추가
        List<Long> addBookIds = bookListService.addBookInBookList(bookListId, request);
        return ApiResponse.onSuccess(BookListConverter.addBookInBookListResultDTO(addBookIds));
    }

    @Operation(summary = "책리스트의 책 삭제 API", description = "책리스트에서 책을 삭제하는 API입니다.")
    @DeleteMapping("list/book/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<?> deleteBookInBookList(
            @PathVariable(name = "bookListId") Long bookListId,
            @RequestBody @Valid BookListRequestDTO.DeleteBookInBookListDTO request) {
        bookListService.deleteBookInBookList(bookListId, request);
        return ApiResponse.onSuccess("책 리스트의 책을 삭제에 성공하였습니다!");
    }

    @Operation(summary = "책리스트 좋아요 API", description = "책리스트에서 좋아요를 추가, 삭제하는 API입니다.")
    @PostMapping("list/likes/{bookListId}")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<?> addLikeToBookList(
            @PathVariable Long bookListId,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        String response = bookListService.toggleLike(bookListId, signInmember);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "인기 책리스트 조회 API", description = "인기 책리스트를 조회하는 API입니다.")
    @GetMapping("list/top")
    public ApiResponse<BookListResponseDTO.TopBookListAndTimeDTO> getTopBookList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        List<BookList> bookLists = bookListService.getTopBookList(page, signInmember);

        // 현재 시간을 구합니다.
        LocalDateTime currentDate = LocalDateTime.now();

        // 책 리스트를 DTO로 변환합니다.
        List<BookListResponseDTO.TopBookListDTO> topBookListDTOs =
                bookLists.stream()
                        .map(
                                bookList ->
                                        BookListConverter.topBookListDTO(
                                                bookList, signInmember.getId()))
                        .collect(Collectors.toList());

        return ApiResponse.onSuccess(
                BookListConverter.topBookListAndTimeDTO(currentDate, topBookListDTOs));
    }

    @Operation(summary = "타사용자 책리스트 추가 API", description = "타사용자 책리스트를 보관함에 추가하는 API입니다.")
    @PostMapping("list/{bookListId}/another")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.AddaAnotherBookListResultDTO>
            addAnotherBookListToLibrary(
                    @PathVariable Long bookListId,
                    @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        BookListResponseDTO.AddaAnotherBookListResultDTO response =
                bookListService.anotherToLibrary(bookListId, signInmember);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "타사용자 책리스트 삭제 API", description = "타사용자 책리스트를 보관함에 삭제하는 API입니다.")
    @DeleteMapping("list/{bookListId}/another")
    @Parameter(name = "bookListId", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<?> deleteAnotherBookListToLibrary(
            @PathVariable Long bookListId,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        bookListService.deleteAnotherBookListToLibrary(bookListId, signInmember);
        return ApiResponse.onSuccess("타사용자 책리스트를 보관함에서 삭제하는데 성공하였습니다!");
    }

    @Operation(summary = "추천 책 조회 API", description = "추천 책 5개를 조회하는 API입니다.")
    @GetMapping("recommend")
    public ApiResponse<BookListResponseDTO.RecommendBookAndTimeDTO> getRecommendBooks() {
        BookListResponseDTO.RecommendBookAndTimeDTO recommendBooks =
                bookListService.getRecommendBooks();
        return ApiResponse.onSuccess(recommendBooks);
    }

    @Operation(
            summary = "보관함 책 조회 API",
            description = "보관함 책을 조회 API입니다. 책을 전체, 읽는중, 완료 별로 따로 조회할 수 있습니다. sortBy로 정렬 가능합니다.")
    @GetMapping("/book")
    public ApiResponse<BookListResponseDTO.LibraryBookDTO> getLibraryBooks(
            @RequestParam(name = "category", defaultValue = "all")
                    @Parameter(description = "책 상태 ['all' - 전체, 'reading' - 읽는중, 'finished'- 완료]")
                    String category,
            @RequestParam(name = "sortBy", defaultValue = "relevance")
                    @Parameter(
                            description =
                                    "정렬 기준 ['newest' - 최신순, 'oldest' - 오래된 순, 'relevance' - 정확도 순, 'rating_desc' - 별점 높은 순, 'rating_asc' - 별점 낮은 순]")
                    String sortBy,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {

        BookListResponseDTO.LibraryBookDTO bookDTOList =
                bookListService.getLibraryBooks(category, sortBy, page, signInmember);
        return ApiResponse.onSuccess(bookDTOList);
    }

    @Operation(
            summary = "DB 책 조회 API",
            description = "DB에 책이 있는지 조회하는 API입니다. 책리스트에 책을 넣기전, db에 있는지 없는지 확인하고 없다면 책 추가하게 돕습니다.")
    @GetMapping("/book/db")
    @Parameter(name = "isbn", description = "책리스트의 아이디, path variable 입니다!")
    public ApiResponse<BookListResponseDTO.DbBookDTO> getDbBook(@RequestParam String isbn) {
        Book dbBook = bookListService.getDbBook(isbn);
        return ApiResponse.onSuccess(BookListConverter.getDbBookDTO(dbBook));
    }
}
