package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberRequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpRequestDTO {

        @NotNull
        @Size(max = 100)
        private String email;

        @Setter @NotNull private String password;

        @NotNull
        @Size(max = 20)
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignInRequestDTO {

        @NotNull
        @Size(max = 100)
        private String email;

        @Setter @NotNull private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestTokenDTO {
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePasswordDTO {
        private String password;
        private String email;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancelAccountReasonDTO {

        @NotNull private String reason;

        private String extraOpinion = "";
    }
}
