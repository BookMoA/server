package com.umc.server.converter;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.web.dto.BookListRequestDTO;
import com.umc.server.web.dto.BookListResponseDTO;
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
    public static BookList toBookList(BookListRequestDTO.AddBookListDTO request, Member member) {
        ListStatus listStatus = convertToListStatus(request.getStatus());

        return BookList.builder()
                .title(request.getTitle())
                .spec(request.getSpec())
                .img(request.getImg())
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
                .id(book.getId())
                .title(book.getTitle())
                .coverImg(book.getCoverImage())
                .writer(book.getWriter())
                .number(bookListEntry.getNumber()) // 책리스트에서의 순서 가져오기
                .build();
    }

    // 책 + 책리스트 조회
    public static BookListResponseDTO.BookListPreviewDTO toBookListPreviewDTO(
            Optional<BookList> optionalBookList) {
        if (optionalBookList.isEmpty()) {
            // Optional이 비어있는 경우에 대한 처리를 여기에 작성합니다.
            throw new GeneralException(ErrorStatus.BOOKLIST_NOT_FOUND);
        }

        BookList bookList = optionalBookList.get();

        List<BookListResponseDTO.BookDTO> getBookDTOList =
                bookList.getBookListEntry().stream()
                        .map(BookListConverter::getBookDTO)
                        .collect(Collectors.toList());

        return BookListResponseDTO.BookListPreviewDTO.builder()
                .id(bookList.getId())
                .title(bookList.getTitle())
                .img(bookList.getImg())
                .spec(bookList.getSpec())
                .like(bookList.getLikeCnt())
                .bookCnt(bookList.getBookCnt())
                .listStatus(bookList.getListStatus().name())
                .nickname(bookList.getMember().getNickname())
                .books(getBookDTOList)
                .build();
    }

    // 책리스트 수정
    public static BookListRequestDTO.UpdateBookListDTO toUpdateBookListDTO(BookList bookList) {
        return BookListRequestDTO.UpdateBookListDTO.builder()
                .title(bookList.getTitle())
                .spec(bookList.getSpec())
                .img(bookList.getImg())
                .status(bookList.getListStatus().name())
                .build();
    }

    public static BookListResponseDTO.LibraryBookListDTO toLibraryBookListDTO(BookList bookList) {
        return BookListResponseDTO.LibraryBookListDTO.builder()
                .id(bookList.getId())
                .title(bookList.getTitle())
                .img(bookList.getImg())
                .bookCnt(bookList.getBookCnt())
                .listStatus(bookList.getListStatus().name())
                .build();
    }
}
