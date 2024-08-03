package com.umc.server.service.MemberService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.repository.MemberRepository;
import com.umc.server.util.JwtTokenUtil;
import com.umc.server.web.dto.request.KakaoRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired PasswordEncoder passwordEncoder;

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

    public KakaoRequestDTO.SignUpRequestDTO getMemberInfo(String accessToken)
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

        return KakaoRequestDTO.SignUpRequestDTO.builder()
                .id(id)
                .nickname(nickname)
                .accessToken(accessToken)
                .profileURL(profileURL)
                .build();
    }

    // TODO: 카카오 회원 회원가입 및 로그인
    public MemberResponseDTO.SignInResponseDTO signUp(
            KakaoRequestDTO.SignUpRequestDTO signUpRequestDTO) {

        // return 할 Member
        Member signInMember = null;

        // 존재하는 회원인지 확인
        final String password = signUpRequestDTO.getId().toString();
        final String encodedPassword = passwordEncoder.encode(password);
        Optional<Member> socialMember =
                memberRepository.findByPasswordAndSignUpType(
                        encodedPassword, SignUpType.valueOf("SOCIAL"));

        if (socialMember.isEmpty()) {
            // 회원가입
            signUpRequestDTO.setPassword(encodedPassword);
            signInMember = memberRepository.save(MemberConverter.toMember(signUpRequestDTO));
        } else {
            // 이미 존재하는 회원의 액세스 토큰 업데이트
            signInMember = socialMember.get();
        }

        MemberResponseDTO.TokenInfo tokenInfo =
                getTokenInfo(signUpRequestDTO.getNickname(), password);
        final String accessToken = tokenInfo.getAccessToken();
        final String refreshToken = tokenInfo.getRefreshToken();

        signInMember.setRefreshToken(refreshToken);
        signInMember.setInActiveDate(LocalDate.now());
        memberRepository.save(signInMember);

        // 로그인 진행
        MemberResponseDTO.SignInResponseDTO signInMemberDTO =
                MemberConverter.toSignInResponseDTO(signInMember);
        signInMemberDTO.setAccessToken(accessToken);

        return signInMemberDTO;
    }

    private MemberResponseDTO.TokenInfo getTokenInfo(String nickname, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(nickname, password);

        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(authentication, nickname);
    }
}
