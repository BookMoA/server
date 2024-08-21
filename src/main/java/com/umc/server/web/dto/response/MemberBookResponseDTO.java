package com.umc.server.web.dto.response;

import com.umc.server.domain.enums.MemberBookStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MemberBookResponseDTO {

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class CreateMemberBookResultDTO {
        private Long memberBookId;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class MemberBookPreviewDTO {
        private String title;

        private String writer;

        private Long memberBookId;

        private MemberBookStatus memberBookStatus;

        private Long readPage;

        private LocalDate startedAt;

        private LocalDate endedAt;

        private Integer score;

        private Long memberId;

        private Long bookId;

        private String image;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class MemberBookPreviewListDTO {
        List<MemberBookPreviewDTO> memberBookPreviewDTOList;
    }
}
