package com.umc.server.service.ClubService;

import com.umc.server.domain.Club;
import com.umc.server.web.dto.request.ClubRequestDTO;
import java.util.Optional;

public interface ClubService {
    Club createClub(ClubRequestDTO.ClubCreateRequestDTO request);

    Optional<Club> readMyClub(Long memberId);

    Club readClubDetail(Long clubId);

    Club updateClub(Long clubId, ClubRequestDTO.ClubUpdateRequestDTO request);

    void deleteClub(Long clubId, Long memberId);
}
