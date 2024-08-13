package com.umc.server.service.ClubPostLikeService;

import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubPostLike;
import com.umc.server.web.dto.request.ClubPostLikeRequestDTO;

public interface ClubPostLikeService {
    ClubPostLike createClubPostLike(
            Member member, ClubPostLikeRequestDTO.ClubPostLikeCreateRequestDTO request);

    void deleteClubPostLike(
            Member member, ClubPostLikeRequestDTO.ClubPostLikeDeleteRequestDTO request);
}
