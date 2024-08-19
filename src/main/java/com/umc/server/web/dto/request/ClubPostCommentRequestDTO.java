package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubPostCommentRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCommentCreateRequestDTO {
        @Positive(message = "clubId cannot be Blank!!")
        Long postId;

        @NotNull(message = "context cannot be Null!!")
        @Size(min = 0, max = 100, message = "context's min = 0, max = 500")
        String context;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCommentDeleteRequestDTO {
        @Positive(message = "postCommentId cannot be Blank!!")
        Long postCommentId;
    }
}
