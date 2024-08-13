package com.umc.server.converter;

import com.umc.server.domain.DailyReading;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.web.dto.response.DailyReadingResponseDTO;
import java.util.List;

public class DailyReadingConverter {

    // 처음 하루 독서량 생성시
    public static DailyReading toDailyReading(MemberBook memberBook) {
        return DailyReading.builder()
                .readPage(memberBook.getReadPage())
                .dailyRead(memberBook.getReadPage())
                .build();
    }

    public static DailyReading toDailyReadingAgain(
            MemberBook memberBook, DailyReading latestDailyReading) {
        return DailyReading.builder()
                .readPage(memberBook.getReadPage())
                .dailyRead(memberBook.getReadPage() - latestDailyReading.getReadPage())
                .build();
    }

    public static DailyReadingResponseDTO.CreateDailyReadingResultDTO toCreateDailyReadingResultDTO(
            DailyReading dailyReading) {
        return DailyReadingResponseDTO.CreateDailyReadingResultDTO.builder()
                .dailyReadingId(dailyReading.getId())
                .memberBookId(dailyReading.getMemberBook().getId())
                .build();
    }

    public static DailyReadingResponseDTO.DailyReadingPreviewDTO toDailyReadingPreviewDTO(
            DailyReading dailyReading) {
        return DailyReadingResponseDTO.DailyReadingPreviewDTO.builder()
                .dailyReadingId(dailyReading.getId())
                .readPage(dailyReading.getReadPage())
                .dailyRead(dailyReading.getReadPage())
                .createdAt(dailyReading.getCreatedAt())
                .updatedAt(dailyReading.getUpdatedAt())
                .memberBookId(dailyReading.getMemberBook().getId())
                .title(dailyReading.getMemberBook().getBook().getTitle())
                .writer(dailyReading.getMemberBook().getBook().getWriter())
                .build();
    }

    public static DailyReadingResponseDTO.DailyReadingPreviewListDTO toDailyReadingPreviewListDTO(
            List<DailyReading> dailyReadingList) {
        List<DailyReadingResponseDTO.DailyReadingPreviewDTO> dailyReadingPreviewDTOList =
                dailyReadingList.stream()
                        .map(DailyReadingConverter::toDailyReadingPreviewDTO)
                        .toList();

        return DailyReadingResponseDTO.DailyReadingPreviewListDTO.builder()
                .dailyReadingPreviewDTOList(dailyReadingPreviewDTOList)
                .build();
    }
}
