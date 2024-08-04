package com.umc.server.converter;

import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;

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
}
