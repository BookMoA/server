package com.umc.server.converter;

import com.umc.server.domain.BookMemo;
import com.umc.server.web.dto.request.BookMemoRequestDTO;
import com.umc.server.web.dto.response.BookMemoResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class BookMemoConverter {

    public static BookMemo toBookMemo(BookMemoRequestDTO.CreateBookMemoDTO createBookMemoDTO) {
        return BookMemo.builder()
                .page(createBookMemoDTO.getPage())
                .body(createBookMemoDTO.getBody())
                .build();
    }

    public static BookMemoResponseDTO.CreateBookMemoResultDTO toCreateBookMemoResultDTO(
            BookMemo bookMemo) {
        return BookMemoResponseDTO.CreateBookMemoResultDTO.builder()
                .bookMemoId(bookMemo.getId())
                .memberBookId(bookMemo.getMemberBook().getId())
                .build();
    }

    public static BookMemoResponseDTO.BookMemoPreviewDTO toBookMemoPreviewDTO(BookMemo bookMemo) {
        return BookMemoResponseDTO.BookMemoPreviewDTO.builder()
                .bookMemoId(bookMemo.getId())
                .page(bookMemo.getPage())
                .body(bookMemo.getBody())
                .memberBookId(bookMemo.getMemberBook().getId())
                .createdAt(bookMemo.getCreatedAt())
                .updatedAt(bookMemo.getUpdatedAt())
                .build();
    }

    public static BookMemoResponseDTO.BookMemoPreviewListDTO toBookMemoPreviewListDTO(
            Page<BookMemo> bookMemoPage) {
        List<BookMemoResponseDTO.BookMemoPreviewDTO> bookMemoPreviewDTOList =
                bookMemoPage.stream()
                        .map(BookMemoConverter::toBookMemoPreviewDTO)
                        .collect(Collectors.toList());

        return BookMemoResponseDTO.BookMemoPreviewListDTO.builder()
                .bookMemoPreviewDTOList(bookMemoPreviewDTOList)
                .build();
    }
}
