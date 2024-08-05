package com.umc.server.web.dto.response;

import java.time.LocalDate;
import lombok.*;

public class SearchResponseDTO {

    // 보관함 책리스트 값 검색결과
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

    // 보관함 메모 값 검색결과
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchMemoResponseDTO {
        private Long id; // 메모 아이디
        private Long bookId;
        private String coverImage;
        private String title;
        private String writer;
        private String memo;
        private LocalDate createdAt;
    }
}
