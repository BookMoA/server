package com.umc.server.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class BookListRequestDTO {

    @Getter
    public static class AddBookListDTO {
        @NotBlank String title;
        @NotNull String spec;
        @NotBlank String img;
        @NotBlank String status;
    }
}
