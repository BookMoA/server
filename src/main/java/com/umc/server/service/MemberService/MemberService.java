package com.umc.server.service.MemberService;

import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.MemberRequestDTO;

public interface MemberService {

    // TODO: 회원가입
    void signUp(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO);

    // TODO: 로그인
    Member signIn(MemberRequestDTO.SignInRequestDTO signInRequestDTO);
}
