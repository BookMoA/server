package com.umc.server.web.dto.request;

import lombok.*;

public class KakaoRequsetDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequestDTO {
        private Long id;
        private String nickname;
        private String profileURL;
        private String accessToken;

        @Setter private String password;
    }
}
