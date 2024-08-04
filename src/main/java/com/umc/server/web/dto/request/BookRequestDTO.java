package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class BookRequestDTO {

    @Getter
    public static class CreateBookDTO {
        @NotNull private String title;

        @NotNull private String writer;

        @NotNull private String publisher;

        @NotNull private String isbn;

        @NotNull private Long page;

        private String coverImage;
    }

    @Getter
    public static class UpdateBookDTO {
        @NotNull private String title;

        @NotNull private String writer;

        private String description;

        @NotNull private String publisher;

        @NotNull private String isbn;

        @NotNull private Long page;

        private String coverImage;
    }
}
