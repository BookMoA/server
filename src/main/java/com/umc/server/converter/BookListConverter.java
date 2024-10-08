package com.umc.server.converter;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.domain.mapping.MemberBookList;
import com.umc.server.web.dto.request.BookListRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookListConverter {

    private BookListConverter() {}

    // 책리스트 추가 결과값
    public static BookListResponseDTO.AddBookListResultDTO toAddBookListResultDTO(
            BookList bookList) {
        return BookListResponseDTO.AddBookListResultDTO.builder()
                .bookListId(bookList.getId())
                .title(bookList.getTitle())
                .createdAt(bookList.getCreatedAt())
                .build();
    }

    // 책리스트 추가 입력값
    public static BookList toBookList(
            BookListRequestDTO.AddBookListDTO request, Member member, String url) {
        ListStatus listStatus = convertToListStatus(request.getStatus());

        return BookList.builder()
                .title(request.getTitle())
                .spec(request.getSpec())
                .img(url)
                .listStatus(listStatus)
                .likeCnt(0)
                .bookCnt(0)
                .member(member)
                .build();
    }

    // 책리스트 ENUM
    private static ListStatus convertToListStatus(String status) {
        switch (status.toUpperCase()) {
            case "PUBLIC":
                return ListStatus.PUBLIC;
            case "PRIVATE":
                return ListStatus.PRIVATE;
            default:
                throw new GeneralException(ErrorStatus.BOOKLIST_INVALID_STATUS);
        }
    }

    // 책 조회
    public static BookListResponseDTO.BookDTO getBookDTO(BookListEntry bookListEntry) {
        Book book = bookListEntry.getBook();
        return BookListResponseDTO.BookDTO.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .coverImg(book.getCoverImage())
                .writer(book.getWriter())
                .number(bookListEntry.getNumber()) // 책리스트에서의 순서 가져오기
                .build();
    }

    // 책 + 책리스트 조회
    public static BookListResponseDTO.BookListPreviewDTO toBookListPreviewDTO(
            Optional<BookList> optionalBookList, Long memberId) {
        if (optionalBookList.isEmpty()) {
            // Optional이 비어있는 경우에 대한 처리를 여기에 작성
            throw new GeneralException(ErrorStatus.BOOKLIST_NOT_FOUND);
        }

        BookList bookList = optionalBookList.get();

        List<BookListResponseDTO.BookDTO> getBookDTOList =
                bookList.getBookListEntry().stream()
                        .map(BookListConverter::getBookDTO)
                        .collect(Collectors.toList());

        // 현재 사용자의 좋아요 상태를 확인하기 위한 필터링
        boolean likeStatus =
                bookList.getMemberBookList().stream()
                        .filter(
                                memberBookList ->
                                        memberBookList.getMember().getId().equals(memberId))
                        .map(MemberBookList::getIsLiked)
                        .findFirst()
                        .orElse(false);

        return BookListResponseDTO.BookListPreviewDTO.builder()
                .bookListId(bookList.getId())
                .title(bookList.getTitle())
                .img(bookList.getImg())
                .spec(bookList.getSpec())
                .likeCnt(bookList.getLikeCnt())
                .bookCnt(bookList.getBookCnt())
                .listStatus(bookList.getListStatus().name())
                .nickname(bookList.getMember().getNickname())
                .likeStatus(likeStatus)
                .books(getBookDTOList)
                .build();
    }

    // 책리스트 수정
    public static BookListRequestDTO.UpdateBookListDTO toUpdateBookListDTO(BookList bookList) {
        return BookListRequestDTO.UpdateBookListDTO.builder()
                .title(bookList.getTitle())
                .spec(bookList.getSpec())
                .status(bookList.getListStatus().name())
                .build();
    }

    public static BookListResponseDTO.UpdateBookListResultDTO toUpdateBookListDTO2(
            BookList bookList) {
        // 기존 책 리스트의 이미지 URL
        String img = bookList.getImg();

        // BookListEntry 리스트를 가져옴
        List<BookListEntry> entries = bookList.getBookListEntry();

        // BookListEntry 리스트를 UpdateBookDTO 리스트로 변환
        List<BookListResponseDTO.UpdateBookDTO> books =
                entries.stream()
                        .map(
                                entry ->
                                        BookListResponseDTO.UpdateBookDTO.builder()
                                                .bookId(entry.getBook().getId())
                                                .number(entry.getNumber())
                                                .build())
                        .collect(Collectors.toList());

        // 현재 시간
        LocalDateTime updatedAt = LocalDateTime.now();

        return BookListResponseDTO.UpdateBookListResultDTO.builder()
                .bookListId(bookList.getId())
                .title(bookList.getTitle())
                .spec(bookList.getSpec())
                .status(bookList.getListStatus().name())
                .img(img)
                .books(books)
                .updatedAt(updatedAt)
                .build();
    }

    public static BookListResponseDTO.LibraryBookListDTO toLibraryBookListDTO(
            BookList bookList, Long memberId) {
        // 현재 사용자의 좋아요 상태를 확인하기 위한 필터링
        boolean likeStatus =
                bookList.getMemberBookList().stream()
                        .filter(
                                memberBookList ->
                                        memberBookList.getMember().getId().equals(memberId))
                        .map(MemberBookList::getIsLiked)
                        .findFirst()
                        .orElse(false);

        boolean storedStatus =
                bookList.getMemberBookList().stream()
                        .filter(
                                memberBookList ->
                                        memberBookList.getMember().getId().equals(memberId))
                        .map(MemberBookList::getIsStored)
                        .findFirst()
                        .orElse(false);

        return BookListResponseDTO.LibraryBookListDTO.builder()
                .bookListId(bookList.getId())
                .title(bookList.getTitle())
                .img(bookList.getImg())
                .likeCnt(bookList.getLikeCnt())
                .bookCnt(bookList.getBookCnt())
                .listStatus(bookList.getListStatus().name())
                .likeStatus(likeStatus)
                .storedStatus(storedStatus)
                .build();
    }

    public static BookListResponseDTO.AddBookInBookListResultDTO addBookInBookListResultDTO(
            List<Long> addBookIds) {
        return BookListResponseDTO.AddBookInBookListResultDTO.builder()
                .addedBookIds(addBookIds)
                .build();
    }

    public static BookListResponseDTO.TopBookListDTO topBookListDTO(
            BookList bookList, long memberId) {
        boolean likeStatus =
                bookList.getMemberBookList().stream()
                        .filter(
                                memberBookList ->
                                        memberBookList.getMember().getId().equals(memberId))
                        .map(MemberBookList::getIsLiked)
                        .findFirst()
                        .orElse(false);

        return BookListResponseDTO.TopBookListDTO.builder()
                .bookListId(bookList.getId())
                .title(bookList.getTitle())
                .img(bookList.getImg())
                .bookCnt(bookList.getBookCnt())
                .likeCnt(bookList.getLikeCnt())
                .listStatus(String.valueOf(bookList.getListStatus()))
                .likeStatus(likeStatus)
                .build();
    }

    public static BookListResponseDTO.TopBookListAndTimeDTO topBookListAndTimeDTO(
            LocalDateTime currentDate, List<BookListResponseDTO.TopBookListDTO> topBookListDTOs) {
        return BookListResponseDTO.TopBookListAndTimeDTO.builder()
                .updatedAt(currentDate)
                .bookLists(topBookListDTOs)
                .build();
    }

    public static BookListResponseDTO.DbBookDTO getDbBookDTO(Book book) {
        return BookListResponseDTO.DbBookDTO.builder().bookId(book.getId()).build();
    }
}
