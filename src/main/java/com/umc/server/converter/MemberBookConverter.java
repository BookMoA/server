package com.umc.server.converter;

import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.web.dto.request.MemberBookRequestDTO;
import com.umc.server.web.dto.response.MemberBookResponseDTO;

public class MemberBookConverter {

    public static MemberBook toMemberBook(
            MemberBookRequestDTO.CreateMemberBookDTO createMemberBookDTO) {
        return MemberBook.builder()
                .readPage(createMemberBookDTO.getReadPage())
                .memberBookStatus(createMemberBookDTO.getMemberBookStatus())
                .endedAt(createMemberBookDTO.getEndedAt())
                .score(createMemberBookDTO.getScore())
                .build();
    }

    public static MemberBookResponseDTO.CreateMemberBookResultDTO toCreateMemberBookResult(
            MemberBook memberBook) {
        return MemberBookResponseDTO.CreateMemberBookResultDTO.builder()
                .memberBookId(memberBook.getId())
                .build();
    }

    public static MemberBookResponseDTO.MemberBookPreviewDTO toMemberBookPreviewDTO(
            MemberBook memberBook) {
        return MemberBookResponseDTO.MemberBookPreviewDTO.builder()
                .memberBookId(memberBook.getId())
                .memberBookStatus(memberBook.getMemberBookStatus())
                .readPage(memberBook.getReadPage())
                .startedAt(memberBook.getStartedAt())
                .endedAt(memberBook.getEndedAt())
                .score(memberBook.getScore())
                .memberId(memberBook.getMember().getId())
                .bookId(memberBook.getBook().getId())
                .build();
    }
}
