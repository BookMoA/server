package com.umc.server.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class BookResponseDTO {

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class CreateBookResultDTO {
        private Long bookId;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class BookPreviewDTO {
        private Long bookId;
        private String title;
        private String writer;
        private String description;
        private String publisher;
        private String isbn;
        private Long page;
        private String coverImage;
    }
}
