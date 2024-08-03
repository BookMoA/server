package com.umc.server.service.MemberService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.repository.MemberRepository;
import com.umc.server.util.PasswordUtil;
import com.umc.server.web.dto.request.KakaoRequsetDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KaKaoServiceImpl implements KakaoService {

    @Value("${KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${KAKAO_REDIRECT_URL}")
    private String redirectUri;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String clientSecret;

    private final MemberRepository memberRepository;

    // TODO: 토큰 받기
    public String getAccessToken(String code) throws JsonProcessingException {

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // request
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kauth.kakao.com/oauth/token",
                        HttpMethod.POST,
                        tokenRequest,
                        String.class);

        // access token parsing
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    public KakaoRequsetDTO.SignUpRequestDTO getMemberInfo(String accessToken)
            throws JsonProcessingException {

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // request
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.POST,
                        userInfoRequest,
                        String.class);

        // response
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode accountNode = jsonNode.path("kakao_account");
        JsonNode profileNode = accountNode.path("profile");

        Long id = jsonNode.path("id").asLong();
        String nickname = profileNode.path("nickname").asText();
        String profileURL = profileNode.path("profile_image_url").asText();

        return KakaoRequsetDTO.SignUpRequestDTO.builder()
                .id(id)
                .nickname(nickname)
                .profileURL(profileURL)
                .accessToken(accessToken)
                .build();
    }

    // TODO: 카카오 회원 회원가입 및 로그인
    public Member signUp(KakaoRequsetDTO.SignUpRequestDTO signUpRequestDTO) {

        // return 할 Member
        Member signInMember = null;

        // 존재하는 회원인지 확인
        final String password = signUpRequestDTO.getId().toString();
        final String encodedPassword = PasswordUtil.encryptPassword(password);
        Optional<Member> socialMember =
                memberRepository.findByPasswordAndSignUpType(
                        encodedPassword, SignUpType.valueOf("SOCIAL"));

        // 회원가입
        if (socialMember.isEmpty()) {
            signUpRequestDTO.setPassword(encodedPassword);
            final Member newMember = MemberConverter.toMember(signUpRequestDTO);
            signInMember = memberRepository.save(newMember);
        } else {
            // 이미 존재하는 회원의 액세스 토큰 업데이트
            Member existingMember = socialMember.get();
            existingMember.setAccessToken(signUpRequestDTO.getAccessToken());
            signInMember = memberRepository.save(existingMember);
        }

        // 로그인 진행
        return signInMember;
    }
}
