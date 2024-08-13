package com.umc.server.service.ClubPostLikeService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.ClubPostLikeConverter;
import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.domain.mapping.ClubPostLike;
import com.umc.server.repository.ClubMemberRepository;
import com.umc.server.repository.ClubPostLikeRepository;
import com.umc.server.repository.ClubPostRepository;
import com.umc.server.web.dto.request.ClubPostLikeRequestDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubPostLikeServiceImpl implements ClubPostLikeService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubPostRepository clubPostRepository;
    private final ClubPostLikeRepository clubPostLikeRepository;

    @Override
    public ClubPostLike createClubPostLike(
            Member member, ClubPostLikeRequestDTO.ClubPostLikeCreateRequestDTO request) {
        // 1. 게시글 존재 확인
        ClubPost clubPost =
                clubPostRepository
                        .findById(request.getPostId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_POST_NOT_FOUND));

        // 2. 클럽 멤버 확인
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 3. 클럽-멤버 확인
        if (!clubPost.getClub().getId().equals(clubMember.getClub().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        // 4. 공감 여부 확인
        Optional<ClubPostLike> clubPostLike =
                clubPostLikeRepository.findByClubPostIdAndMemberId(
                        clubPost.getId(), member.getId());

        // 4. 공감
        return clubPostLike.orElseGet(
                () ->
                        clubPostLikeRepository.save(
                                ClubPostLikeConverter.toClubPostLike(member, clubPost)));
    }

    @Override
    public void deleteClubPostLike(
            Member member, ClubPostLikeRequestDTO.ClubPostLikeDeleteRequestDTO request) {
        // 1. 공감 확인
        Optional<ClubPostLike> clubPostLike =
                clubPostLikeRepository.findByClubPostIdAndMemberId(
                        request.getPostId(), member.getId());

        // 3. 공감 취소
        clubPostLike.ifPresent(clubPostLikeRepository::delete);
    }
}
