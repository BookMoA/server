package com.umc.server.converter;

import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.web.dto.BookListRequestDTO;
import com.umc.server.web.dto.BookListResponseDTO;

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
                throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }
}
