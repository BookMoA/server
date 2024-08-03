package com.umc.server.service.MemberService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.KakaoRequsetDTO;

public interface KakaoService {
    String getAccessToken(String code) throws JsonProcessingException;

    Member signUp(KakaoRequsetDTO.SignUpRequestDTO signUpRequestDTO);

    KakaoRequsetDTO.SignUpRequestDTO getMemberInfo(String accessToken)
            throws JsonProcessingException;
}
