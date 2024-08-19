package com.umc.server.converter;

import com.umc.server.domain.Book;
import com.umc.server.web.dto.request.BookRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import com.umc.server.web.dto.response.BookResponseDTO;

public class BookConverter {

    public static Book toBook(BookRequestDTO.CreateBookDTO createBookDTO, String imgUrl) {
        return Book.builder()
                .title(createBookDTO.getTitle())
                .writer(createBookDTO.getWriter())
                .publisher(createBookDTO.getPublisher())
                .isbn(createBookDTO.getIsbn())
                .page(createBookDTO.getPage())
                .coverImage(imgUrl)
                .build();
    }

    public static BookResponseDTO.CreateBookResultDTO toCreateBookResultDTO(Book book) {
        return BookResponseDTO.CreateBookResultDTO.builder().bookId(book.getId()).build();
    }

    public static BookResponseDTO.BookPreviewDTO toBookPreviewDTO(Book book) {
        return BookResponseDTO.BookPreviewDTO.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .writer(book.getWriter())
                .description(book.getDescription())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .page(book.getPage())
                .coverImage(book.getCoverImage())
                .build();
    }

    // 추천 책
    public static BookListResponseDTO.RecommendBookDTO toRecommendBookDTO(Book book) {
        return BookListResponseDTO.RecommendBookDTO.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .writer(book.getWriter())
                .coverImage(book.getCoverImage())
                .build();
    }
}
