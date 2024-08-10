package com.umc.server.service.ClubMemberService;

import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.web.dto.request.ClubMemberRequestDTO;
import java.util.List;

public interface ClubMemberService {
    ClubMember createClubMember(
            Member member, ClubMemberRequestDTO.ClubMemberCreateRequestDTO request);

    List<ClubMember> readClubMember(Member member, Long clubId);

    ClubMember updateClubMember(
            Member member, ClubMemberRequestDTO.ClubMemberUpdateRequestDTO request);

    void dropClubMember(Member reader, ClubMemberRequestDTO.ClubMemberDropRequestDTO request);

    void deleteClubMember(Member member);

    void deleteReaderClubMember(
            Member reader, ClubMemberRequestDTO.ClubMemberDeleteReaderRequestDTO request);
}
