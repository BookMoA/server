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
}
