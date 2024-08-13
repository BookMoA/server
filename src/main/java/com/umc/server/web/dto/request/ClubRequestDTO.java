package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubCreateRequestDTO {

        @Positive(message = "Member ID must be positive")
        Long memberId;

        @NotBlank(message = "name cannot be Blank!!")
        @Size(min = 1, max = 50, message = "name's min = 1, max = 50")
        String name;

        @NotNull(message = "intro cannot be Null!!")
        @Size(min = 0, max = 100, message = "intro's min = 0, max = 100")
        String intro;

        @NotNull(message = "notice cannot be Null!!")
        @Size(min = 0, max = 200, message = "notice's min = 0, max = 200")
        String notice;

        @NotNull(message = "password cannot be Null!!")
        @Size(min = 0, max = 10, message = "password's min = 0, max = 10")
        String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubUpdateRequestDTO {
        @Positive(message = "memebrId cannot be Blank!!")
        Long memberId;

        @NotNull(message = "intro cannot be Null!!")
        @Size(min = 0, max = 100, message = "intro's min = 0, max = 100")
        String intro;

        @NotNull(message = "notice cannot be Null!!")
        @Size(min = 0, max = 200, message = "notice's min = 0, max = 200")
        String notice;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubDeleteRequestDTO {
        @Positive(message = "clubId cannot be Blank!!")
        Long clubId;

        @Positive(message = "memebrId cannot be Blank!!")
        Long memberId;
    }
}
