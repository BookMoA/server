package com.umc.server.converter;

import com.umc.server.domain.Member;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;

public class MemberConverter {

    // 회원가입시 받은 정보를 member 인스턴스로 변환
    public static Member toMember(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .signUpType(SignUpType.GENERAL)
                .build();
    }

    // 로그인 후 멤버 정보를 responseDTO로 변환
    public static MemberResponseDTO.SignInResponseDTO toSignInResponseDTO(Member member) {
        return MemberResponseDTO.SignInResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .inFocusMode(member.getInFocusMode())
                .totalPages(member.getTotalPages())
                .signUpType(member.getSignUpType())
                .accessToken(member.getAccessToken())
                .profileURL(member.getProfileURL())
                .build();
    }
}
