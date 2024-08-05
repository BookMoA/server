package com.umc.server.service.MemberService;

import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;

public interface MemberService {

    // TODO: 회원가입
    MemberResponseDTO.SignInResponseDTO signUp(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO);

    // TODO: 로그인
    MemberResponseDTO.SignInResponseDTO signIn(MemberRequestDTO.SignInRequestDTO signInRequestDTO);

    // TODO: 토큰 만료시 재발급
    MemberResponseDTO.TokenInfo renew(String accessToken, String refreshToken);

    // TODO: 로그아웃
    void signOut(Member signInMember);

    // TODO: 닉네임 존재 여부 확인
    Boolean nicknameExist(String nickname);
}
