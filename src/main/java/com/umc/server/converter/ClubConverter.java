package com.umc.server.converter;

import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.ClubRequestDTO;
import com.umc.server.web.dto.response.ClubResponseDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@NoArgsConstructor
public class ClubConverter {
    public static Club toClub(ClubRequestDTO.ClubCreateRequestDTO request, String code) {
        return Club.builder()
                .name(request.getName())
                .intro(request.getIntro())
                .notice(request.getNotice())
                .code(code)
                .password(request.getPassword())
                .build();
    }

    public static ClubResponseDTO.ClubCreateResponseDTO toClubCreateResponseDTO(Club club) {
        return ClubResponseDTO.ClubCreateResponseDTO.builder().clubId(club.getId()).build();
    }

    public static ClubResponseDTO.MyClubResponseDTO toMyClubResponseDTO(
            Member member, Optional<Club> club) {
        if (club.isPresent()) {
            return ClubResponseDTO.MyClubResponseDTO.builder()
                    .memeberId(member.getId())
                    .clubId(club.get().getId())
                    .name(club.get().getName())
                    .intro(club.get().getIntro())
                    .reader(member.getClubMember().getReader())
                    .build();
        } else {
            return new ClubResponseDTO.MyClubResponseDTO();
        }
    }

    public static ClubResponseDTO.ClubDetailResponseDTO toClubDetailResponseDTO(Club club) {
        return ClubResponseDTO.ClubDetailResponseDTO.builder()
                .clubId(club.getId())
                .name(club.getName())
                .intro(club.getIntro())
                .notice(club.getNotice())
                .code(club.getCode())
                .password(club.getPassword())
                .createAt(club.getCreatedAt())
                .updateAt(club.getUpdatedAt())
                .memberCount(club.getClubMemberList().size())
                .build();
    }

    public static ClubResponseDTO.ClubUpdateResponseDTO toClubUpdateResponseDTO(Club club) {
        return ClubResponseDTO.ClubUpdateResponseDTO.builder()
                .clubId(club.getId())
                .name(club.getName())
                .intro(club.getIntro())
                .notice(club.getNotice())
                .code(club.getCode())
                .password(club.getPassword())
                .createAt(club.getCreatedAt())
                .updateAt(club.getUpdatedAt())
                .build();
    }

    public static ClubResponseDTO.ClubPreviewResponseDTO toClubPreviewResponseDTO(Club club) {
        return ClubResponseDTO.ClubPreviewResponseDTO.builder()
                .clubId(club.getId())
                .name(club.getName())
                .intro(club.getIntro())
                .createAt(club.getCreatedAt())
                .updateAt(club.getUpdatedAt())
                .memberCount(club.getClubMemberList().size())
                .postCount(club.getClubPostList().size())
                .build();
    }

    public static ClubResponseDTO.ClubRecommendResponseDTO toClubRecommendResponseDTO(
            Slice<Club> clubSlice, String category, Integer page) {
        List<ClubResponseDTO.ClubPreviewResponseDTO> clubList = new ArrayList<>();
        for (Club club : clubSlice.getContent()) {
            clubList.add(toClubPreviewResponseDTO(club));
        }
        return ClubResponseDTO.ClubRecommendResponseDTO.builder()
                .category(category)
                .page(page)
                .size(clubSlice.getSize())
                .clubList(clubList)
                .build();
    }

    public static ClubResponseDTO.ClubSearchResponseDTO toClubSearchResponseDTO(
            Page<Club> clubPage, String category, String word, Integer page) {
        List<ClubResponseDTO.ClubPreviewResponseDTO> clubList = new ArrayList<>();
        for (Club club : clubPage.getContent()) {
            clubList.add(toClubPreviewResponseDTO(club));
        }
        return ClubResponseDTO.ClubSearchResponseDTO.builder()
                .category(category)
                .word(word)
                .page(page)
                .totalElements((int) clubPage.getTotalElements())
                .totalPages(clubPage.getTotalPages())
                .size(clubPage.getSize())
                .clubList(clubList)
                .build();
    }
}
