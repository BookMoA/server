package com.umc.server.converter;

import com.umc.server.domain.BookList;
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
}
