package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
        @Positive(message = "clubId cannot be Blank!!")
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
        @Positive(message = "clubId cannot be Blank!!")
        Long clubId;

        @NotNull(message = "statusMessage cannot be Null!!")
        @Size(min = 0, max = 100, message = "statusMessage's min = 0, max = 100")
        String statusMessage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberDropRequestDTO {
        @Positive(message = "memebrId cannot be Blank!!")
        Long memberId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberDeleteReaderRequestDTO {
        @Positive(message = "readerId cannot be Blank!!")
        Long newReaderId;
    }
}
