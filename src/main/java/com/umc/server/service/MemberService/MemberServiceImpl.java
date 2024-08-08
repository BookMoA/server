package com.umc.server.service.MemberService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.PushNotification;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.domain.enums.TokenStatus;
import com.umc.server.repository.AdminMemberRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.repository.PushNotificationRepository;
import com.umc.server.util.JwtTokenUtil;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import io.jsonwebtoken.Claims;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PushNotificationRepository pushNotificationRepository;
    private final JavaMailSender emailSender;
    private final AdminMemberRepository adminMemberRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Value("${NAVER_ID}")
    private String NAVER_ID;

    // TODO: 회원가입 기능
    public MemberResponseDTO.SignInResponseDTO signUp(
            MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {

        // 존재하는 회원인지 확인
        final String email = signUpRequestDTO.getEmail();
        final Boolean isExistingMember = memberRepository.existsByEmail(email);
        if (isExistingMember) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        // password 암호화 진행
        final String password = signUpRequestDTO.getPassword();
        final String encodedPassword = passwordEncoder.encode(password);
        signUpRequestDTO.setPassword(encodedPassword);
        final Member newMember = MemberConverter.toMember(signUpRequestDTO);

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
        Member signInMember = memberRepository.save(newMember);

        return getToken(password, signInMember);
    }

    // TODO: 로그인
    public MemberResponseDTO.SignInResponseDTO signIn(
            MemberRequestDTO.SignInRequestDTO signInRequestDTO) {

        // 이메일 확인
        final String email = signInRequestDTO.getEmail();

        Member signInMember =
                memberRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀번호 확인
        final String password = signInRequestDTO.getPassword();
        final String encodedPassword = passwordEncoder.encode(password);
        if (passwordEncoder.matches(signInMember.getPassword(), encodedPassword)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return getToken(password, signInMember);
    }

    // TODO: 토큰 생성
    private MemberResponseDTO.SignInResponseDTO getToken(String password, Member signInMember) {
        try {
            final String nickname = signInMember.getNickname();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(nickname, password);
            Authentication authentication =
                    authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            MemberResponseDTO.TokenInfo tokenInfo =
                    jwtTokenUtil.generateToken(authentication, nickname);

            final String accessToken = tokenInfo.getAccessToken();
            final String refreshToken = tokenInfo.getRefreshToken();

            signInMember.setRefreshToken(refreshToken);
            signInMember = memberRepository.save(signInMember);

            MemberResponseDTO.SignInResponseDTO signInMemberDTO =
                    MemberConverter.toSignInResponseDTO(signInMember);
            signInMemberDTO.setAccessToken(accessToken);

            return signInMemberDTO;
        } catch (AuthenticationException e) {
            throw new MemberHandler(ErrorStatus._UNAUTHORIZED);
        }
    }

    // TODO: 토큰 재발급
    public MemberResponseDTO.TokenInfo renew(String accessToken, String refreshToken) {
        MemberResponseDTO.TokenInfo tokenInfo = null;

        if (jwtTokenUtil.validateToken(refreshToken) == TokenStatus.valueOf("ACCESS")) {

            final Claims claims = jwtTokenUtil.parseClaims(accessToken);
            final String nickname = claims.get("nickname", String.class);
            final Member member =
                    memberRepository
                            .findByNickname(nickname)
                            .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
            final String savedToken = member.getRefreshToken();

            if (savedToken.matches(refreshToken)) {
                Authentication authentication = jwtTokenUtil.getAuthentication(accessToken);
                tokenInfo = jwtTokenUtil.generateToken(authentication, nickname);

                member.setRefreshToken(tokenInfo.getRefreshToken());
                memberRepository.save(member);
            }
        } else {
            throw new MemberHandler(ErrorStatus.INVALID_TOKEN_ERROR);
        }
        return tokenInfo;
    }

    // TODO: 로그아웃
    public void signOut(Member signInMember) {
        if (signInMember.getSignUpType() == SignUpType.SOCIAL) {
            final String accessToken = signInMember.getKakaoAccessToken();

            if (accessToken != null && !accessToken.isEmpty()) {
                try {
                    Long kakaoId = kakaoDisconnect(accessToken);
                    if (!kakaoId.equals(signInMember.getKakaoId())) {
                        throw new MemberHandler(ErrorStatus._BAD_REQUEST);
                    }
                    signInMember.setKakaoAccessToken(null);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        signInMember.setRefreshToken(null);
        memberRepository.save(signInMember);
    }

    private Long kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // request
        HttpEntity<MultiValueMap<String, String>> kakaoSignOutRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v1/user/logout",
                        HttpMethod.POST,
                        kakaoSignOutRequest,
                        String.class);

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("id").asLong();
    }

    // TODO: 닉네임 존재 여부 확인
    public Boolean nicknameExist(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    // TODO: 알림 동의 여부 변경
    public MemberResponseDTO.PushNotification setNotification(
            Long memberId, Boolean likePush, Boolean commentPush, Boolean nightPush) {
        PushNotification pushNotification =
                pushNotificationRepository
                        .findByMemberId(memberId)
                        .orElseThrow(
                                () -> new MemberHandler(ErrorStatus.PUSH_NOTIFICATION_NOT_FOUND));

        if (likePush != null) {
            pushNotification.setLikePushEnabled(likePush);
        }
        if (commentPush != null) {
            pushNotification.setCommentPushEnabled(commentPush);
        }
        if (nightPush != null) {
            pushNotification.setNightPushEnabled(nightPush);
        }
        PushNotification updatedNotification = pushNotificationRepository.save(pushNotification);

        return MemberResponseDTO.PushNotification.of(
                updatedNotification.getLikePushEnabled(),
                updatedNotification.getCommentPushEnabled(),
                updatedNotification.getNightPushEnabled());
    }

    // TODO: 이메일로 인증번호 보내기
    public MemberResponseDTO.CodeDTO sendCode(String email) throws MessagingException {
        Boolean isExist = memberRepository.existsByEmailAndSignUpType(email, SignUpType.GENERAL);
        if (!isExist) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        // 난수 생성
        Random random = new Random();
        final String code = String.format("%04d", random.nextInt(10000));

        // 이메일 전송
        MimeMessage message = mailBody(email, code);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            throw new MemberHandler(ErrorStatus.EMAIL_SEND_ERROR);
        }

        return MemberResponseDTO.CodeDTO.of(code, LocalDateTime.now());
    }

    // 이메일 내용 만들기
    private MimeMessage mailBody(String email, String code) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email); // 받는 사람
        message.setSubject("[책모아] 비밀번호 재설정을 위한 인증번호가 도착했습니다"); // 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1 style='color:#2E7D32; font-size:30px; font-weight:bold;'>비밀번호 재설정 인증번호</h1>";
        msgg += "<br>";
        msgg += "<p style='color:#212121; font-size:17px; line-height:1.5;'>";
        msgg += "안녕하세요.<br>";
        msgg += "요청하신 비밀번호 재설정 인증번호입니다.<br>";
        msgg += "아래의 인증번호를 입력하여 비밀번호 재설정을 완료해 주세요.";
        msgg += "</p>";
        msgg += "<br>";
        msgg +=
                "<div style='font-size:54px; font-weight:bold; text-align:center; background-color:#F5F5F5; padding:20px; border-radius:10px;'>";
        msgg += code;
        msgg += "</div>";
        msgg += "<br>";
        msgg += "<p style='color:#757575; font-size:13px; line-height:1.5;'>";
        msgg += "본 이메일은 비밀번호 재설정을 위해 발송되었습니다.<br>";
        msgg += "만약 본인이 요청하지 않았다면 이 메일을 무시하셔도 됩니다.<br>";
        msgg += "인증번호는 5분 동안 유효합니다.";
        msgg += "</p>";
        msgg += "</div>";
        msgg +=
                "<div style='margin-top:50px; padding-top:20px; border-top:1px solid #EEEEEE; font-size:12px; color:#757575;'>";
        msgg += "© 2024 UMC 책모아. All rights reserved.<br>";
        msgg += "book-moa@naver.com";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom(NAVER_ID);

        return message;
    }

    // TODO: 비밀번호 변경하기
    public void changePassword(MemberRequestDTO.ChangePasswordDTO changePasswordDTO) {
        Member member =
                memberRepository
                        .findByEmail(changePasswordDTO.getEmail())
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        final String newPassword = passwordEncoder.encode(changePasswordDTO.getPassword());
        member.setPassword(newPassword);
        memberRepository.save(member);
    }

    // TODO: 책모아 팀원 정보 얻기
    public List<MemberResponseDTO.AdminMemberResponseDTO> getAdminInfo() {
        return adminMemberRepository.findAll().stream()
                .map(
                        adminMember ->
                                MemberResponseDTO.AdminMemberResponseDTO.builder()
                                        .adminRole(adminMember.getAdminRole())
                                        .emailAddress(adminMember.getEmailAddress())
                                        .nickName(adminMember.getNickName())
                                        .githubId(adminMember.getGithubId())
                                        .profileUrl(adminMember.getProfileUrl())
                                        .build())
                .collect(Collectors.toList());
    }
}
