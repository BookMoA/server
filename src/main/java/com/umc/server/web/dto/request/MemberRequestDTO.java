package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class MemberRequestDTO {

    @Getter
    public static class SignUpRequestDTO {

        @NotBlank
        @Size(max = 100)
        String email;

        @Setter @NotBlank String password;

        @NotBlank
        @Size(max = 20)
        String nickname;
    }

    @Getter
    public static class SignInRequestDTO {

        @NotBlank
        @Size(max = 100)
        String email;

        @Setter @NotBlank String password;
    }

    @Getter
    public static class RequestTokenDTO {
        String refreshToken;
    }
}
