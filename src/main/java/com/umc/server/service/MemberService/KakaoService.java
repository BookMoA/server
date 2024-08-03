package com.umc.server.service.MemberService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.server.web.dto.request.KakaoRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;

public interface KakaoService {
    String getAccessToken(String code) throws JsonProcessingException;

    MemberResponseDTO.SignInResponseDTO signUp(KakaoRequestDTO.SignUpRequestDTO signUpRequestDTO);

    KakaoRequestDTO.SignUpRequestDTO getMemberInfo(String accessToken)
            throws JsonProcessingException;
}
