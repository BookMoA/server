package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DailyReadingResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class CreateDailyReadingResultDTO {
        private Long dailyReadingId;

        private Long memberBookId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class DailyReadingPreviewDTO {
        private Long dailyReadingId;

        private Long readPage;

        private Long dailyRead;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private Long memberBookId;

        private String title;

        private String writer;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class DailyReadingPreviewListDTO {
        List<DailyReadingPreviewDTO> dailyReadingPreviewDTOList;
    }
}
