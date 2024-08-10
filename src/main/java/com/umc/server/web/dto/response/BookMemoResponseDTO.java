package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class BookMemoResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class CreateBookMemoResultDTO {
        private Long bookMemoId;

        private Long memberBookId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class BookMemoPreviewDTO {
        private Long bookMemoId;

        private Long page;

        private String body;

        private Long memberBookId;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class BookMemoPreviewListDTO {
        List<BookMemoPreviewDTO> bookMemoPreviewDTOList;
    }
}
