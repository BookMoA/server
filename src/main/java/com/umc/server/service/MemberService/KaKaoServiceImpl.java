package com.umc.server.service.MemberService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.PushNotification;
import com.umc.server.repository.MemberRepository;
import com.umc.server.util.JwtTokenUtil;
import com.umc.server.web.dto.request.KakaoRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
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
                .kakaoId(id)
                .nickname(nickname)
                .profileURL(profileURL)
                .build();
    }

    // TODO: 카카오 회원 회원가입 및 로그인
    public MemberResponseDTO.SignInResponseDTO signUp(
            KakaoRequestDTO.SignUpRequestDTO signUpRequestDTO, String kakaoAccessToken) {

        // return 할 Member
        Member signInMember = null;

        // 존재하는 회원인지 확인
        final Long kakaoId = signUpRequestDTO.getKakaoId();
        Optional<Member> socialMember = memberRepository.findByKakaoId(kakaoId);

        if (socialMember.isEmpty()) {
            // 회원가입
            final String encodedPassword = passwordEncoder.encode(kakaoId.toString());
            signUpRequestDTO.setPassword(encodedPassword);
            Member newMember = MemberConverter.toMember(signUpRequestDTO);

            // 알림 정보 생성
            PushNotification newPushNotification =
                    PushNotification.builder()
                            .commentPushEnabled(Boolean.TRUE)
                            .likePushEnabled(Boolean.TRUE)
                            .nightPushEnabled(Boolean.TRUE)
                            .build();

            // 두 엔티티 연결
            newMember.setPushNotification(newPushNotification);
            newPushNotification.setMember(newMember);
            signInMember = memberRepository.save(newMember);

        } else {
            // 이미 존재하는 회원의 액세스 토큰 업데이트
            signInMember = socialMember.get();
        }

        MemberResponseDTO.TokenInfo tokenInfo =
                getTokenInfo(signUpRequestDTO.getNickname(), kakaoId.toString());
        final String accessToken = tokenInfo.getAccessToken();
        final String refreshToken = tokenInfo.getRefreshToken();
        final LocalDateTime accessExpired =
                LocalDateTime.ofInstant(
                        jwtTokenUtil.parseAccessClaims(accessToken).getExpiration().toInstant(),
                        ZoneId.systemDefault());
        final LocalDateTime refreshExpired =
                LocalDateTime.ofInstant(
                        jwtTokenUtil.parseRefreshClaims(refreshToken).getExpiration().toInstant(),
                        ZoneId.systemDefault());

        signInMember.setRefreshToken(refreshToken);
        signInMember.setKakaoAccessToken(kakaoAccessToken);
        memberRepository.save(signInMember);

        // 로그인 진행
        MemberResponseDTO.SignInResponseDTO signInMemberDTO =
                MemberConverter.toSignInResponseDTO(signInMember);
        signInMemberDTO.setAccessToken(accessToken);
        signInMemberDTO.setAccessExpiredDate(accessExpired);
        signInMemberDTO.setRefreshExpiredDate(refreshExpired);

        return signInMemberDTO;
    }

    private MemberResponseDTO.TokenInfo getTokenInfo(String nickname, String password) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(nickname, password);

            Authentication authentication =
                    authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            return jwtTokenUtil.generateToken(authentication, nickname);
        } catch (AuthenticationException e) {
            throw new MemberHandler(ErrorStatus._UNAUTHORIZED);
        }
    }
}
