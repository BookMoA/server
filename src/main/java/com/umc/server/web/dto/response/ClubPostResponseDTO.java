package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubPostResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCreateResponseDTO {
        Long postId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostDetailResponseDTO {
        Long postId;

        Long memberId;

        String title;

        String nickname;

        String context;

        Integer likeCount;

        LocalDateTime createAt;

        LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostListResponseDTO {
        Long clubId;

        Integer page;

        Integer size;

        @Builder.Default
        List<ClubPostResponseDTO.ClubPostDetailResponseDTO> postList = new ArrayList<>();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostSearchResponseDTO {
        Long clubId;

        String category;

        String word;

        Integer page;

        Integer totalElements;

        Integer totalPages;

        Integer size;

        @Builder.Default
        List<ClubPostResponseDTO.ClubPostDetailResponseDTO> postList = new ArrayList<>();
    }
}
