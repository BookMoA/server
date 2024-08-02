package com.umc.server.converter;

import com.umc.server.domain.Book;
import com.umc.server.web.dto.BookRequestDTO;
import com.umc.server.web.dto.BookResponseDTO;

public class BookConverter {

    public static Book toBook(BookRequestDTO.CreateBookDTO createBookDTO) {
        return Book.builder()
                .title(createBookDTO.getTitle())
                .writer(createBookDTO.getWriter())
                .publisher(createBookDTO.getPublisher())
                .isbn(createBookDTO.getIsbn())
                .page(createBookDTO.getPage())
                .coverImage(createBookDTO.getCoverImage())
                .build();
    }

    public static BookResponseDTO.CreateBookResultDTO toCreateBookResultDTO(Book book) {
        return BookResponseDTO.CreateBookResultDTO.builder().bookId(book.getId()).build();
    }
}
