package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubMemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberCreateResponseDTO {
        Long clubMemberId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubMemberDetailResponseDTO {
        Long memberId;

        String nickname;

        Boolean reader;

        String statusMessage;

        LocalDateTime createdAt;

        LocalDateTime updateAt;
    }
}
