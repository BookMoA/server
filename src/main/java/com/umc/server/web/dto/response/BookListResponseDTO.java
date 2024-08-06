package com.umc.server.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
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

    // 책리스트 상세 값 읽기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookListPreviewDTO {
        private Long id;
        private String title;
        private String img;
        private String spec;
        private int likeCnt;
        private int bookCnt;
        private String listStatus;
        private String nickname;
        private boolean likeStatus; // 좋아요 여부
        private List<BookDTO> books;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDTO {
        private Long id;
        private String title;
        private String coverImg;
        private String writer;
        private int number;
    }

    // 보관함 책리스트 값 읽기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LibraryBookListDTO {
        private Long id;
        private String title;
        private String img;
        private int likeCnt;
        private int bookCnt;
        private String listStatus;
        private boolean likeStatus;
        private boolean storedStatus;
    }

    //
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddBookInBookListResultDTO {
        private List<Long> addedBookIds;
    }

    // 책리스트 상세 값 읽기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopBookListAndTimeDTO {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;

        private List<TopBookListDTO> bookLists;
    }

    // 인기 책리스트 값 읽기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopBookListDTO {
        private Long id;
        private String title;
        private String img;
        private int bookCnt;
        private int likeCnt;
        private String listStatus;
        private boolean likeStatus;
    }

    // 타사용자 책리스트 추가 결과값
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddaAnotherBookListResultDTO {
        Long memberBookId;
        LocalDateTime createdAt;
    }
}
