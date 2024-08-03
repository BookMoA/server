package com.umc.server.web.dto.response;

import com.umc.server.domain.enums.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private String accessToken;
        private String profileURL;
    }
}
