package com.umc.server.service.ClubPostCommentService;

import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubPostComment;
import com.umc.server.web.dto.request.ClubPostCommentRequestDTO;
import org.springframework.data.domain.Slice;

public interface ClubPostCommentService {
    ClubPostComment createClubPostComment(
            Member member, ClubPostCommentRequestDTO.ClubPostCommentCreateRequestDTO request);

    Slice<ClubPostComment> readClubPostCommentList(Member member, Long postId, Integer page);

    void deleteClubPostComment(
            Member member, ClubPostCommentRequestDTO.ClubPostCommentDeleteRequestDTO request);
}
