package com.umc.server.converter;

import com.umc.server.domain.Member;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.web.dto.request.KakaoRequsetDTO;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberConverter {

    // 회원가입시 받은 정보를 member 인스턴스로 변환
    public static Member toMember(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .signUpType(SignUpType.valueOf("GENERAL"))
                .build();
    }

    // 로그인 후 멤버 정보를 responseDTO로 변환
    public static MemberResponseDTO.SignInResponseDTO toSignInResponseDTO(Member member) {
        return MemberResponseDTO.SignInResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .inFocusMode(
                        member.getInFocusMode() == null ? Boolean.FALSE : member.getInFocusMode())
                .totalPages(member.getTotalPages() == null ? 0L : member.getTotalPages())
                .signUpType(member.getSignUpType())
                .accessToken(member.getAccessToken())
                .profileURL(member.getProfileURL())
                .build();
    }

    // 소셜 로그인한 객체를 member 인스턴스로 변환
    public static Member toMember(KakaoRequsetDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .profileURL(signUpRequestDTO.getProfileURL())
                .accessToken(signUpRequestDTO.getAccessToken())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .signUpType(SignUpType.valueOf("SOCIAL"))
                .inActiveDate(LocalDate.from(LocalDateTime.now()))
                .build();
    }
}
