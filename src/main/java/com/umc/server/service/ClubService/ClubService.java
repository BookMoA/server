package com.umc.server.service.ClubService;

import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.ClubRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface ClubService {
    Club createClub(Member member, ClubRequestDTO.ClubCreateRequestDTO request);

    Optional<Club> readMyClub(Member member);

    Club readClubDetail(Long clubId);

    Club updateClub(Member member, ClubRequestDTO.ClubUpdateRequestDTO request);

    void deleteClub(Member member, ClubRequestDTO.ClubDeleteRequestDTO request);

    Slice<Club> recommendClub(String category, Integer page);

    Page<Club> searchClub(String category, String word, Integer page);
}
