package com.umc.server.service.ClubPostService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.ClubPostConverter;
import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.repository.ClubMemberRepository;
import com.umc.server.repository.ClubPostRepository;
import com.umc.server.web.dto.request.ClubPostRequestDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubPostServiceImpl implements ClubPostService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubPostRepository clubPostRepository;

    @Override
    @Transactional
    public ClubPost createClubPost(
            Member member, ClubPostRequestDTO.ClubPostCreateRequestDTO request) {
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!request.getClubId().equals(clubMember.getClub().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        return clubPostRepository.save(
                ClubPostConverter.toClubPost(member, clubMember.getClub(), request));
    }

    @Override
    public Optional<ClubPost> readClubPost(Member member, Long postId) {
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        Optional<ClubPost> clubPost = clubPostRepository.findById(postId);
        if (clubPost.isPresent()) {
            if (!clubPost.get().getClub().equals(clubMember.getClub())) {
                throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
            }
        }
        return clubPost;
    }

    @Override
    public Slice<ClubPost> readClubPostList(Member member, Long clubId, Integer page) {
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!clubId.equals(clubMember.getClub().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        PageRequest pageRequest =
                PageRequest.of((int) (page - 1), 10, Sort.by("createdAt").descending());

        return clubPostRepository.findAll(pageRequest);
    }

    @Override
    public Page<ClubPost> searchClubPost(
            Member member, String category, String word, Integer page) {
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        //        if (!clubId.equals(clubMember.getClub().getId())){
        //            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        //        }
        // 특정 clubId에서만 겁색
        Page<ClubPost> postList;
        String keyword = word.replace(" ", ".*");
        PageRequest pageRequest =
                PageRequest.of((int) (page - 1), 10, Sort.by("createdAt").descending());

        if (category.equals("context")) {
            postList = clubPostRepository.findByContextContaining(keyword, pageRequest);
        } else if (category.equals("title")) {
            postList = clubPostRepository.findByTitleContaining(keyword, pageRequest);
        } else if (category.equals("writer")) {
            postList = clubPostRepository.findByTitleContaining(keyword, pageRequest);
            // postList = clubPostRepository.findByNickNameContaining(keyword, pageRequest);
        } else {
            throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        return postList;
    }

    @Override
    @Transactional
    public ClubPost updateClubPost(
            Member member, ClubPostRequestDTO.ClubPostUpdateRequestDTO request) {
        ClubPost clubPost =
                clubPostRepository
                        .findById(request.getPostId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_POST_NOT_FOUND));
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!clubPost.getClub().equals(clubMember.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }
        if (!clubPost.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.CLUB_POST_WRITER_REQUIRED);
        }
        clubPost.setTitle(request.getTitle());
        clubPost.setContext(request.getContext());
        return clubPost;
    }

    @Override
    @Transactional
    public void deleteClubPost(Member member, ClubPostRequestDTO.ClubPostDeleteRequestDTO request) {
        ClubPost clubPost =
                clubPostRepository
                        .findById(request.getPostId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_POST_NOT_FOUND));
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!clubPost.getClub().equals(clubMember.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }
        if (!clubPost.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.CLUB_POST_WRITER_REQUIRED);
        }
        clubPostRepository.delete(clubPost);
    }
}
