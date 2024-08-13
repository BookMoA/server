package com.umc.server.web.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubPostLikeRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostLikeCreateRequestDTO {
        @Positive(message = "postId cannot be Blank!!")
        Long postId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostLikeDeleteRequestDTO {
        @Positive(message = "postId cannot be Blank!!")
        Long postId;
    }
}
