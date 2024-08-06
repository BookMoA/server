package com.umc.server.web.dto.response;

import com.umc.server.domain.enums.SignUpType;
import lombok.*;

public class MemberResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInResponseDTO {

        private Long id;
        private String email;
        private String nickname;
        private Boolean inFocusMode;
        private Long totalPages;
        private SignUpType signUpType;
        @Setter private String accessToken;
        @Setter private String refreshToken;
        private String profileURL;
    }

    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        String accessToken;
        String refreshToken;

        public static TokenInfo of(String accessToken, String refreshToken) {
            return new TokenInfo(accessToken, refreshToken);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UniqueNickname {
        Boolean isUnique;

        public static UniqueNickname of(Boolean isUnique) {
            return new UniqueNickname(isUnique);
        }
    }
}
