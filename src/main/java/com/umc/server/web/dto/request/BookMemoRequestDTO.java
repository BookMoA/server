package com.umc.server.web.dto.request;

import lombok.Getter;

public class BookMemoRequestDTO {

    @Getter
    public static class CreateBookMemoDTO {
        private Long page;

        private String body;
    }

    @Getter
    public static class UpdateBookMemoDTO {
        private Long page;

        private String body;
    }
}
