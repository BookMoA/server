package com.umc.server.service.ClubPostCommentService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.ClubPostCommentConverter;
import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.domain.mapping.ClubPostComment;
import com.umc.server.repository.ClubMemberRepository;
import com.umc.server.repository.ClubPostCommentRepository;
import com.umc.server.repository.ClubPostRepository;
import com.umc.server.web.dto.request.ClubPostCommentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubPostCommentServiceImpl implements ClubPostCommentService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubPostRepository clubPostRepository;
    private final ClubPostCommentRepository clubPostCommentRepository;

    @Override
    @Transactional
    public ClubPostComment createClubPostComment(
            Member member, ClubPostCommentRequestDTO.ClubPostCommentCreateRequestDTO request) {
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

        if (!clubPost.getClub().getId().equals(clubMember.getClub().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        // 3. 댓글 생성
        return clubPostCommentRepository.save(
                ClubPostCommentConverter.toClubPostComment(member, clubPost, request));
    }

    @Override
    public Slice<ClubPostComment> readClubPostCommentList(
            Member member, Long postId, Integer page) {
        // 1. 게시글 존재 확인
        ClubPost clubPost =
                clubPostRepository
                        .findById(postId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_POST_NOT_FOUND));

        // 2. 클럽 멤버 확인
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        if (!clubPost.getClub().getId().equals(clubMember.getClub().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        PageRequest pageRequest =
                PageRequest.of((int) (page - 1), 10, Sort.by("createdAt").descending());

        // 3. 댓글 조회
        return clubPostCommentRepository.findAllByClubPostId(postId, pageRequest);
    }

    @Override
    @Transactional
    public void deleteClubPostComment(
            Member member, ClubPostCommentRequestDTO.ClubPostCommentDeleteRequestDTO request) {
        // 1. 댓글 존재 확인
        ClubPostComment comment =
                clubPostCommentRepository
                        .findById(request.getPostCommentId())
                        .orElseThrow(
                                () ->
                                        new GeneralException(
                                                ErrorStatus.CLUB_POST_COMMENT_NOT_FOUND));

        // 2. 클럽 멤버 & 작성자 확인
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!comment.getClubPost().getClub().getId().equals(clubMember.getClub().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }
        if (!comment.getMember().getId().equals(clubMember.getMember().getId())) {
            throw new GeneralException(ErrorStatus.CLUB_POST_COMMENT_WRITER_REQUIRED);
        }

        // 3. 댓글 삭제
        clubPostCommentRepository.delete(comment);
    }
}
