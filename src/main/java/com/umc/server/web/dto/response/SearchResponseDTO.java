package com.umc.server.web.dto.response;

import java.time.LocalDate;
import lombok.*;

public class SearchResponseDTO {

    // 보관함 책리스트 값 읽기!
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchBookListResponseDTO {
        private Long id;
        private String title;
        private String img;
        private int likeCnt;
        private int bookCnt;
        private LocalDate createdAt;
    }
}
