package com.umc.server.service.MemberService;

import com.umc.server.web.dto.request.MemberRequestDTO;

public interface MemberService {

    // TODO: 회원가입
    void signUp(MemberRequestDTO.SignUpRequestDTO signUpRequestDTO);
}
