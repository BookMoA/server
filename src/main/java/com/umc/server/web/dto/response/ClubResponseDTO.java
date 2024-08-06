package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ClubResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubCreateResponseDTO {
        Long clubId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyClubResponseDTO {
        Long clubId;

        String name;

        String intro;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPreviewResponseDTO {
        Long clubId;

        String name;

        String intro;

        LocalDateTime createAt;

        LocalDateTime updateAt;

        Integer memberCount;

        Integer postCount;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubDetailResponseDTO extends ClubUpdateResponseDTO {
        Integer memberCount;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubUpdateResponseDTO {
        Long clubId;

        String name;

        String intro;

        String notice;

        String code;

        String password;

        LocalDateTime createAt;

        LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubRecommendResponseDTO {
        String category;

        Integer page;

        Integer size;

        @Builder.Default List<ClubPreviewResponseDTO> clubList = new ArrayList<>();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubSearchResponseDTO {
        String category;

        String word;

        Integer page;

        Integer totalElements;

        Integer totalPages;

        Integer size;

        @Builder.Default List<ClubPreviewResponseDTO> clubList = new ArrayList<>();
    }
}
