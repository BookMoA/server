package com.umc.server.service.ClubMemberService;

import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.web.dto.request.ClubMemberRequestDTO;
import java.util.List;

public interface ClubMemberService {
    ClubMember createClubMember(
            Long memberId, ClubMemberRequestDTO.ClubMemberCreateRequestDTO request);

    List<ClubMember> readClubMember(Long memberId, Long clubId);

    ClubMember updateClubMember(
            Long memberId, ClubMemberRequestDTO.ClubMemberUpdateRequestDTO request);
}
