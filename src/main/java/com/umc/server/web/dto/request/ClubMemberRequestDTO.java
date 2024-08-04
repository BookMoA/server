package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubMemberRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberCreateRequestDTO {
        @NotBlank(message = "clubId cannot be Blank!!")
        Long clubId;

        @NotNull(message = "password cannot be Null!!")
        @Size(min = 0, max = 10, message = "password's min = 0, max = 10")
        String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberUpdateRequestDTO {
        @NotBlank(message = "clubId cannot be Blank!!")
        Long clubId;

        @NotNull(message = "statusMessage cannot be Null!!")
        @Size(min = 0, max = 100, message = "statusMessage's min = 0, max = 100")
        String statusMessage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberDeleteRequestDTO {
        @NotBlank(message = "clubId cannot be Blank!!")
        Long clubId;

        @NotBlank(message = "memebrId cannot be Blank!!")
        Long memberId;
    }
}
