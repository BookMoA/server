package com.umc.server.converter;

import com.umc.server.domain.Club;
import com.umc.server.web.dto.request.ClubRequestDTO;
import com.umc.server.web.dto.response.ClubResponseDTO;
import java.util.Optional;
import lombok.NoArgsConstructor;

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

    public static ClubResponseDTO.MyClubResponseDTO toMyClubResponseDTO(Optional<Club> club) {
        if (club.isPresent()) {
            return ClubResponseDTO.MyClubResponseDTO.builder()
                    .clubId(club.get().getId())
                    .name(club.get().getName())
                    .intro(club.get().getIntro())
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
}
