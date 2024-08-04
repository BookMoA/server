package com.umc.server.converter;

import com.umc.server.domain.Member;
import com.umc.server.domain.enums.Role;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.web.dto.request.KakaoRequestDTO;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import java.time.LocalDate;

public class MemberConverter {

    // 회원가입시 받은 정보를 member 인스턴스로 변환
    public static Member toMember(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .signUpType(SignUpType.valueOf("GENERAL"))
                .role(Role.valueOf("USER"))
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
                .profileURL(member.getProfileURL())
                .refreshToken(member.getRefreshToken())
                .build();
    }

    // 소셜 로그인한 객체를 member 인스턴스로 변환
    public static Member toMember(KakaoRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .profileURL(signUpRequestDTO.getProfileURL())
                .password(signUpRequestDTO.getPassword())
                .nickname(signUpRequestDTO.getNickname())
                .refreshToken(signUpRequestDTO.getRefreshToken())
                .signUpType(SignUpType.valueOf("SOCIAL"))
                .inActiveDate(LocalDate.now())
                .kakaoId(signUpRequestDTO.getKakaoId())
                .role(Role.valueOf("USER"))
                .build();
    }
}
