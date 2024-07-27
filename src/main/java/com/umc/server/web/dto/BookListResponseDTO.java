package com.umc.server.web.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookListResponseDTO {

    // 책리스트 추가 결과값
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddBookListResultDTO {
        Long bookListId;
        String title;
        LocalDateTime createdAt;
    }
}
