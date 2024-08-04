package com.umc.server.web.dto.request;

import lombok.*;

public class KakaoRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequestDTO {
        private Long kakaoId;
        private String nickname;
        private String profileURL;
        private String accessToken;
        @Setter private String refreshToken;
        @Setter private String password;
    }
}
