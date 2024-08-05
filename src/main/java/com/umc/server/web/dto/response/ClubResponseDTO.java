package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ClubResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubCreateResponseDTO {
        Long clubId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyClubResponseDTO {
        Long clubId;

        String name;

        String intro;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPreviewResponseDTO {
        Long clubId;

        String name;

        String intro;

        Integer memberCount;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubDetailResponseDTO extends ClubUpdateResponseDTO {
        Integer memberCount;
    }

    @SuperBuilder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubUpdateResponseDTO {
        Long clubId;

        String name;

        String intro;

        String notice;

        String code;

        String password;

        LocalDateTime createAt;

        LocalDateTime updateAt;
    }
}
