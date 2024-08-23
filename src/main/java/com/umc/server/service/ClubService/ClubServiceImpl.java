package com.umc.server.service.ClubService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.ClubConverter;
import com.umc.server.converter.ClubMemberConverter;
import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.repository.ClubMemberRepository;
import com.umc.server.repository.ClubRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.request.ClubRequestDTO;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
public class ClubServiceImpl implements ClubService {
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    @Transactional
    public Club createClub(Member member, ClubRequestDTO.ClubCreateRequestDTO request) {
        // 1. 모임장 상태 유효 판별 (존재 & 모임 가입 여부)
        clubMemberRepository
                .findByMemberId(member.getId())
                .ifPresent(
                        clubMember -> {
                            throw new GeneralException(ErrorStatus.CLUB_ALREADY_JOINED);
                        });

        // 2. 중복 모임 판별 (이름 동일X)
        clubRepository
                .findByName(request.getName())
                .ifPresent(
                        clubMember -> {
                            throw new GeneralException(ErrorStatus.CLUB_NAME_ALREADY_EXISTS);
                        });

        // 3. 모임 생성 (+ 난수 코드)
        String code;
        do {
            code = generateCode();
        } while (clubRepository.findByCode(code).isPresent());
        Club club = clubRepository.save(ClubConverter.toClub(request, code));

        // 4. 모임장 추가
        clubMemberRepository.save(ClubMemberConverter.toClubMember(club, member, true, null));

        return club;
    }

    @Override
    public Optional<Club> readMyClub(Member member) {
        return clubMemberRepository
                .findByMemberId(member.getId())
                .flatMap(clubMember -> clubRepository.findById(clubMember.getClub().getId()));
    }

    @Override
    public Club readClubDetail(Long clubId) {
        return clubRepository
                .findById(clubId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));
    }

    @Override
    @Transactional
    public Club updateClub(Member member, ClubRequestDTO.ClubUpdateRequestDTO request) {
        // 1. 모임 존재 판별
        Club club =
                clubRepository
                        .findById(request.getClubId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));

        // 2. 멤버 상태 판별 (존재, 모임 가입 여부 + 모임 동일 여부 + 모임장 여부)
        // 2-2. 모임 가입 여부
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 2-3. 모임 동일 여부
        if (!club.equals(clubMember.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }
        // 2-4. 모임장 여부
        if (!clubMember.getReader()) {
            throw new GeneralException(ErrorStatus.CLUB_LEADER_REQUIRED);
        }

        // 3. 모임 정보 수정
        club.setIntro(request.getIntro());
        club.setNotice(request.getNotice());
        return club;
    }

    @Override
    @Transactional
    public void deleteClub(Member member, ClubRequestDTO.ClubDeleteRequestDTO request) {
        System.out.println("deleteClub");
        if (request.getClubId() == null) {
            System.out.println("clubId == null");
            request.setClubId(Long.valueOf(""));
        }

        // 1. 모임 존재 판별
        Club club =
                clubRepository
                        .findById(request.getClubId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_FOUND));

        // 2. 멤버 상태 판별 (존재, 모임 가입 + 모임장)
        // 2-2. 모임 가입 여부
        ClubMember clubMember =
                clubMemberRepository
                        .findByMemberId(member.getId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.CLUB_NOT_JOINED));

        // 2-3. 모임 동일 여부
        if (!club.equals(clubMember.getClub())) {
            throw new GeneralException(ErrorStatus.CLUB_MEMBER_REQUIRED);
        }
        // 2-4. 모임장 여부
        if (!clubMember.getReader()) {
            throw new GeneralException(ErrorStatus.CLUB_LEADER_REQUIRED);
        }

        // 3. 모임 삭제
        // 자식 엔티티 삭제 (명시적)
        club.getClubMemberList().clear(); // 모든 연관된 ClubMember 삭제
        club.getClubPostList().clear(); // 모든 연관된 ClubPost 삭제

        System.out.println("clubRepository");
        clubRepository.delete(club);
    }

    @Override
    public Slice<Club> recommendClub(String category, Integer page) {
        Slice<Club> clubList;
        PageRequest pageRequest =
                PageRequest.of((int) (page - 1), 10, Sort.by("createdAt").descending());

        if (category.equals("new")) {
            clubList = clubRepository.findAll(pageRequest);
        } else if (category.equals("activity")) {
            clubList = clubRepository.findAllOrderByClubPostCountDesc(pageRequest);
        } else if (category.equals("deadline")) {
            clubList = clubRepository.findAllOrderByClubMemberCountDesc(pageRequest);
        } else {
            throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        return clubList;
    }

    @Override
    public Page<Club> searchClub(String category, String word, Integer page) {
        Page<Club> clubList;
        String keyword = word.replace(" ", ".*");
        PageRequest pageRequest =
                PageRequest.of((int) (page - 1), 10, Sort.by("createdAt").descending());

        if (category.equals("name")) {
            clubList = clubRepository.findByNameContaining(keyword, pageRequest);
        } else if (category.equals("notice")) {
            clubList = clubRepository.findByNoticeContaining(keyword, pageRequest);
        } else {
            throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        return clubList;
    }

    private String generateCode() {

        String LETTERS = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();

        // 대문자 3자를 랜덤으로 선택
        List<Character> characters = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            characters.add(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }

        // 현재 시간의 밀리초를 문자열로 변환하여 3자리로 맞추고 문자 리스트에 추가
        String timestampPart =
                String.format(
                        "%0" + 3 + "d",
                        Integer.parseInt(DateTimeFormatter.ofPattern("SSS").format(Instant.now())));
        for (char c : timestampPart.toCharArray()) {
            if (c == '0') {
                characters.add((char) ('1' + random.nextInt(10)));
            } else characters.add(c);
        }

        // 문자를 무작위로 섞고 하나의 문자열로 조합
        Collections.shuffle(characters);
        StringBuilder sb = new StringBuilder();
        for (char c : characters) {
            sb.append(c);
        }

        return sb.toString();
    }
}
