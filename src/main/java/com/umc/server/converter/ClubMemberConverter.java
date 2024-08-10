package com.umc.server.converter;

import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.web.dto.response.ClubMemberResponseDTO;
import java.util.ArrayList;
import java.util.List;

public class ClubMemberConverter {
    public static ClubMember toClubMember(
            Club club, Member member, Boolean reader, String statusMessage) {
        return ClubMember.builder()
                .club(club)
                .member(member)
                .reader(reader)
                .statusMessage(statusMessage)
                .build();
    }

    public static ClubMemberResponseDTO.ClubMemberCreateResponseDTO toClubMemberCreateResponseDTO(
            ClubMember clubMember) {
        return ClubMemberResponseDTO.ClubMemberCreateResponseDTO.builder()
                .clubMemberId(clubMember.getId())
                .build();
    }

    public static ClubMemberResponseDTO.ClubMemberDetailResponseDTO toClubMemberDetailResponseDTO(
            ClubMember clubMember) {
        return ClubMemberResponseDTO.ClubMemberDetailResponseDTO.builder()
                .memberId(clubMember.getMember().getId())
                .nickname(clubMember.getMember().getNickname())
                .reader(clubMember.getReader())
                .statusMessage(clubMember.getStatusMessage())
                .createdAt(clubMember.getCreatedAt())
                .updateAt(clubMember.getUpdatedAt())
                .build();
    }

    public static List<ClubMemberResponseDTO.ClubMemberDetailResponseDTO>
            toClubMemberListResponseDTO(List<ClubMember> clubMembers) {
        List<ClubMemberResponseDTO.ClubMemberDetailResponseDTO> clubMemberList = new ArrayList<>();
        for (ClubMember cm : clubMembers) {
            clubMemberList.add(toClubMemberDetailResponseDTO(cm));
        }
        return clubMemberList;
    }
}
