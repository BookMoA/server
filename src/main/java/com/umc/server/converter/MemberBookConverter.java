package com.umc.server.converter;

import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.web.dto.request.MemberBookRequestDTO;
import com.umc.server.web.dto.response.MemberBookResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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
                .title(memberBook.getBook().getTitle())
                .writer(memberBook.getBook().getWriter())
                .memberBookId(memberBook.getId())
                .memberBookStatus(memberBook.getMemberBookStatus())
                .readPage(memberBook.getReadPage())
                .startedAt(memberBook.getStartedAt())
                .endedAt(memberBook.getEndedAt())
                .score(memberBook.getScore())
                .memberId(memberBook.getMember().getId())
                .bookId(memberBook.getBook().getId())
                .image(memberBook.getBook().getCoverImage())
                .build();
    }

    public static MemberBookResponseDTO.MemberBookPreviewListDTO toMemberBookPreviewListDTO(
            Page<MemberBook> memoMemberBookPage) {
        List<MemberBookResponseDTO.MemberBookPreviewDTO> memberBookPreviewDTOList =
                memoMemberBookPage.stream()
                        .map(MemberBookConverter::toMemberBookPreviewDTO)
                        .collect(Collectors.toList());

        return MemberBookResponseDTO.MemberBookPreviewListDTO.builder()
                .memberBookPreviewDTOList(memberBookPreviewDTOList)
                .build();
    }
}
