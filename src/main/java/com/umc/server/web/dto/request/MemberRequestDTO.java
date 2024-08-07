package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
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

        @NotBlank
        @Size(max = 100)
        private String email;

        @Setter @NotBlank private String password;

        @NotBlank
        @Size(max = 20)
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignInRequestDTO {

        @NotBlank
        @Size(max = 100)
        private String email;

        @Setter @NotBlank private String password;
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
}
