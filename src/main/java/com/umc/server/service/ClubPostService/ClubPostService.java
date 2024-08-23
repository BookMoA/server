package com.umc.server.service.ClubPostService;

import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.ClubPostRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface ClubPostService {
    ClubPost createClubPost(Member member, ClubPostRequestDTO.ClubPostCreateRequestDTO request);

    Optional<ClubPost> readClubPost(Member member, Long postId);

    Slice<ClubPost> readClubPostList(Member member, Long clubId, Integer page);

    Page<ClubPost> searchClubPost(
            Member member, Long clubId, String category, String word, Integer page);

    ClubPost updateClubPost(Member member, ClubPostRequestDTO.ClubPostUpdateRequestDTO request);

    void deleteClubPost(Member member, ClubPostRequestDTO.ClubPostDeleteRequestDTO request);
}
