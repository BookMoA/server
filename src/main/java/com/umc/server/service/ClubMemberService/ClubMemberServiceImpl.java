package com.umc.server.service.ClubMemberService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.ClubMemberConverter;
import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.repository.ClubMemberRepository;
import com.umc.server.repository.ClubRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.request.ClubMemberRequestDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberServiceImpl implements ClubMemberService {
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    @Transactional
    public ClubMember createClubMember(
            Long memberId, ClubMemberRequestDTO.ClubMemberCreateRequestDTO request) {
        // 1. 사용자 상태 유효 판별 (존재 & 모임 가입 여부)
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        clubMemberRepository
                .findByMemberId(member.getId())
                .ifPresent(
                        cm -> {
                            throw new GeneralException(ErrorStatus.CLUB_ALREADY_JOINED);
                        });

        // 3. 모임 상태 판별 (존재, 모임원 수)
        Club club =
                clubRepository
                        .findById(request.getClubId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));
        if (club.getClubMemberList().size() >= 20) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_FULLED);
        }

        // 3. 모임 가입
        ClubMember clubMember = ClubMemberConverter.toClubMember(club, member, false, null);
        return clubMemberRepository.save(clubMember);
    }

    @Override
    public List<ClubMember> readClubMember(Long memberId, Long clubId) {
        // 1. 사용자 상태 유효 판별 (존재)
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 2. 모임 상태 판별 (존재)
        Club club =
                clubRepository
                        .findById(clubId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));

        // 3. 모임 동일 여부 판단
        if (!club.equals(member.getClubMember().getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        // 4. 모임 멤버들 반환
        return clubMemberRepository.findAllByClubId(club.getId());
    }

    @Override
    @Transactional
    public ClubMember updateClubMember(
            Long memberId, ClubMemberRequestDTO.ClubMemberUpdateRequestDTO request) {
        // 1. 사용자 상태 유효 판별 (존재)
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 2-2. 모임 가입 여부
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 3. 모임 멤버 정보 수정
        clubMember.setStatusMessage(request.getStatusMessage());
        return clubMember;
    }
}
