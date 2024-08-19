package com.umc.server.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookListRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddBookListDTO {
        @NotBlank
        @Size(min = 1, max = 12)
        private String title;

        @NotBlank
        @Size(min = 1, max = 52)
        private String spec;

        @NotBlank private String status;
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
        @NotEmpty private List<BookListEntryDTO> books;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddBookInBookListDTO {
        @NotEmpty private List<Long> booksId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteBookInBookListDTO {
        @NotEmpty private List<Long> booksId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookListEntryDTO {
        private Long bookId; // Book의 ID
        private Integer number; // 새로운 number 값
    }
}
