package com.umc.server.web.dto.request;

import com.umc.server.domain.enums.MemberBookStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;

public class MemberBookRequestDTO {
    @Getter
    public static class CreateMemberBookDTO {
        @NotNull private Long readPage;

        @NotNull private MemberBookStatus memberBookStatus;

        @NotNull private LocalDate endedAt;

        @NotNull private Integer score;

        @NotNull private Long bookId;
    }

    @Getter
    public static class UpdateMemberBookDTO {
        @NotNull private Long readPage;

        @NotNull private MemberBookStatus memberBookStatus;

        @NotNull private LocalDate endedAt;

        @NotNull private Integer score;
    }
}
