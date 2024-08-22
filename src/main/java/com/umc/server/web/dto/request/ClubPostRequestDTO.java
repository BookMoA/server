package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
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
        @NotNull(message = "clubId cannot be Null!!")
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
        @NotNull(message = "postId cannot be Null!!")
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
        @NotNull(message = "postId cannot be Null!!")
        Long postId;
    }
}
