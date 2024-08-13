package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubPostRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCreateRequestDTO {
        @Positive(message = "clubId cannot be Blank!!")
        Long clubId;

        @NotNull(message = "title cannot be Null!!")
        @Size(min = 0, max = 100, message = "title's min = 0, max = 100")
        String title;

        @NotNull(message = "context cannot be Null!!")
        @Size(min = 0, max = 500, message = "context's min = 0, max = 500")
        String context;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostUpdateRequestDTO {
        @Positive(message = "postId cannot be Blank!!")
        Long postId;

        @NotNull(message = "title cannot be Null!!")
        @Size(min = 0, max = 100, message = "title's min = 0, max = 100")
        String title;

        @NotNull(message = "context cannot be Null!!")
        @Size(min = 0, max = 500, message = "context's min = 0, max = 500")
        String context;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostDeleteRequestDTO {
        @Positive(message = "postId cannot be Blank!!")
        Long postId;
    }
}
