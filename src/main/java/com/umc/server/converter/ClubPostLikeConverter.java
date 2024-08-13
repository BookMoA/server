package com.umc.server.converter;

import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubPostLike;
import com.umc.server.web.dto.response.ClubPostLikeResponseDTO;

public class ClubPostLikeConverter {
    public static ClubPostLike toClubPostLike(Member member, ClubPost clubPost) {
        return ClubPostLike.builder().member(member).clubPost(clubPost).build();
    }

    public static ClubPostLikeResponseDTO.ClubPostLikeCreateResponseDTO
            toClubPostLikeCreateResponseDTO(ClubPostLike clubPostLike) {
        return ClubPostLikeResponseDTO.ClubPostLikeCreateResponseDTO.builder()
                .postLikeId(clubPostLike.getId())
                .build();
    }
}
