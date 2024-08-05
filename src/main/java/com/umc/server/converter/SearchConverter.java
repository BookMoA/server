package com.umc.server.converter;

import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.BookMemo;
import com.umc.server.web.dto.response.SearchResponseDTO;

public class SearchConverter {

    public static SearchResponseDTO.SearchBookListResponseDTO searchBookListResponseDTO(
            BookList bookList) {
        return SearchResponseDTO.SearchBookListResponseDTO.builder()
                .id(bookList.getId())
                .title(bookList.getTitle())
                .img(bookList.getImg())
                .likeCnt(bookList.getLikeCnt())
                .bookCnt(bookList.getBookCnt())
                .createdAt(bookList.getCreatedAt().toLocalDate())
                .build();
    }

    public static SearchResponseDTO.SearchMemoResponseDTO toSearchMemoResponseDTO(
            BookMemo bookMemo) {
        Book book = bookMemo.getMemberBook().getBook(); // BookMemo에서 Book 정보를 가져옴

        return SearchResponseDTO.SearchMemoResponseDTO.builder()
                .id(bookMemo.getId())
                .bookId(book.getId())
                .title(book.getTitle())
                .coverImage(book.getCoverImage())
                .writer(book.getWriter())
                .memo(bookMemo.getBody())
                .createdAt(bookMemo.getCreatedAt().toLocalDate())
                .build();
    }
}
