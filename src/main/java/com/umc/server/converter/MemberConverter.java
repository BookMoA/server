package com.umc.server.converter;

import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.MemberRequestDTO;

public class MemberConverter {

    // 회원가입시 받은 정보를 member 인스턴스로 변환
    public static Member toMember(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {
        return Member.builder()
                .email(signUpRequestDTO.getEmail())
                .nickname(signUpRequestDTO.getNickname())
                .build();
    }
}
