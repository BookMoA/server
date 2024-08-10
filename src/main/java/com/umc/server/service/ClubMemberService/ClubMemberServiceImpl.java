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
import java.util.Objects;
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
            Member member, ClubMemberRequestDTO.ClubMemberCreateRequestDTO request) {
        // 1. 사용자 상태 유효 판별 (모임 가입 여부)
        clubMemberRepository
                .findByMemberId(member.getId())
                .ifPresent(
                        cm -> {
                            throw new GeneralException(ErrorStatus.CLUB_ALREADY_JOINED);
                        });

        // 3. 모임 상태 판별 (존재, 비밀번호 동일, 모임원 수)
        Club club =
                clubRepository
                        .findById(request.getClubId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));

        if (!Objects.equals(club.getPassword(), request.getPassword())) {
            throw new GeneralException(ErrorStatus.CLUB_INCORRECT_PASSWORD);
        }

        if (club.getClubMemberList().size() >= 20) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_FULLED);
        }

        // 3. 모임 가입
        ClubMember clubMember = ClubMemberConverter.toClubMember(club, member, false, null);
        return clubMemberRepository.save(clubMember);
    }

    @Override
    public List<ClubMember> readClubMember(Member member, Long clubId) {
        // 1. 모임 상태 판별 (존재)
        Club club =
                clubRepository
                        .findById(clubId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));

        // 2. 사용자 모임 여부 판단
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!Objects.equals(club, clubMember.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        // 3. 모임 멤버들 반환
        return clubMemberRepository.findAllByClubId(club.getId());
    }

    @Override
    @Transactional
    public ClubMember updateClubMember(
            Member member, ClubMemberRequestDTO.ClubMemberUpdateRequestDTO request) {
        // 1. 모임 가입 여부
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 3. 모임 멤버 정보 수정
        clubMember.setStatusMessage(request.getStatusMessage());
        return clubMember;
    }

    @Override
    @Transactional
    public void dropClubMember(
            Member reader, ClubMemberRequestDTO.ClubMemberDropRequestDTO request) {
        // 1. 멤버(모임장) 상태 판별 (모임 가입 + 모임장)
        ClubMember clubReader =
                clubMemberRepository
                        .findByMemberId(reader.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!clubReader.getReader()) {
            throw new GeneralException(ErrorStatus.CLUB_LEADER_REQUIRED);
        }

        // 2. 멤버(모임원) 상태 판별 (존재, 모임 가입)
        // 2-2. 모임 가입 여부
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(request.getMemberId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 2-3. 모임 동일 여부
        if (!clubReader.getClub().equals(clubMember.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        // 3. 모임 멤버 삭제
        clubMemberRepository.delete(clubMember);
    }

    @Override
    @Transactional
    public void deleteClubMember(Member member) {
        // checkClubMember(member);
        // 1. 멤버(모임원) 상태 판별 (모임 가입)
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        // 1-2. 모임원 여부
        if (clubMember.getReader()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        // 3. 모임 멤버 삭제
        System.out.println("Deleting club member: " + member.getId());
        clubMemberRepository.delete(clubMember);
        System.out.println("Club member deleted: " + member.getId());
    }

    @Override
    @Transactional
    public void deleteReaderClubMember(
            Member reader, ClubMemberRequestDTO.ClubMemberDeleteReaderRequestDTO request) {
        // 1. 멤버(모임장) 상태 판별 (모임 가입 + 모임장)
        ClubMember clubReader =
                clubMemberRepository
                        .findByMemberId(reader.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));
        if (!clubReader.getReader()) {
            throw new GeneralException(ErrorStatus.CLUB_LEADER_REQUIRED);
        }

        // 2. 멤버(모임원) 상태 판별 (존재, 모임 가입)
        // 2-1. 멤버(모임원) 존재 여부
        /*Member newReader =
        memberRepository
                .findById(request.getNewReaderId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));*/
        // 2-2. 모임 가입 여부
        ClubMember clubNewReader =
                clubMemberRepository
                        .findByMemberId(request.getNewReaderId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 2-3. 모임 동일 여부
        if (!clubReader.getClub().equals(clubNewReader.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }

        // 3. 모임장 변경
        clubNewReader.setReader(true);

        // 4. 모임 멤버 삭제
        clubMemberRepository.delete(clubReader);
    }
}
