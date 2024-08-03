package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookListRequestDTO {

    @Getter
    public static class AddBookListDTO {
        @NotBlank String title;
        @NotNull String spec;
        @NotBlank String img;
        @NotBlank String status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBookListDTO {
        @NotBlank String title;
        @NotNull String spec;
        @NotBlank String img;
        @NotBlank String status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddBookInBookListDTO {
        @NotBlank private List<Long> booksId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteBookInBookListDTO {
        @NotBlank private List<Long> booksId;
    }
}