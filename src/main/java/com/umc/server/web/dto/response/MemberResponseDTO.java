package com.umc.server.web.dto.response;

import com.umc.server.domain.enums.AdminRole;
import com.umc.server.domain.enums.SignUpType;
import java.time.LocalDateTime;
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
        private MemberResponseDTO.PushNotification pushNotification;
    }

    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;

        public static TokenInfo of(String accessToken, String refreshToken) {
            return new TokenInfo(accessToken, refreshToken);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UniqueNickname {
        private Boolean isUnique;

        public static UniqueNickname of(Boolean isUnique) {
            return new UniqueNickname(isUnique);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PushNotification {

        private Boolean likePush;
        private Boolean comment;
        private Boolean nightPush;

        public static PushNotification of(
                Boolean likePush, Boolean commentPush, Boolean nightPush) {
            return new PushNotification(likePush, commentPush, nightPush);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class EditProfileInfo {

        private String email;
        private String nickname;
        @Setter private String profileURL;

        public static EditProfileInfo of(String email, String nickname, String profileURL) {
            return new EditProfileInfo(email, nickname, profileURL);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CodeDTO {

        private String code;
        private LocalDateTime generateTime;

        public static CodeDTO of(String code, LocalDateTime generateTime) {
            return new CodeDTO(code, generateTime);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminMemberResponseDTO {

        private AdminRole adminRole;
        private String nickName;
        private String githubId;
        private String emailAddress;
        private String snsAddress;
        private String profileUrl;
    }
}
