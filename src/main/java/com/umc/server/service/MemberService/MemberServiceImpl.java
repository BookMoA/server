package com.umc.server.service.MemberService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.TokenStatus;
import com.umc.server.repository.MemberRepository;
import com.umc.server.util.JwtTokenUtil;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired PasswordEncoder passwordEncoder;

    // TODO: 회원가입 기능
    public MemberResponseDTO.SignInResponseDTO signUp(
            MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {

        // 존재하는 회원인지 확인
        final String email = signUpRequestDTO.getEmail();
        final Boolean isExistingMember = memberRepository.existsByEmail(email);
        if (isExistingMember) {
            throw new MemberHandler(ErrorStatus.valueOf("MEMBER_ALREADY_EXISTS"));
        }

        // password 암호화 진행
        final String password = signUpRequestDTO.getPassword();
        final String encodedPassword = passwordEncoder.encode(password);
        signUpRequestDTO.setPassword(encodedPassword);

        final Member newMember = MemberConverter.toMember(signUpRequestDTO);
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
                        .orElseThrow(
                                () -> new MemberHandler(ErrorStatus.valueOf("MEMBER_NOT_FOUND")));

        // 비밀번호 확인
        final String password = signInRequestDTO.getPassword();
        final String encodedPassword = passwordEncoder.encode(password);
        if (passwordEncoder.matches(signInMember.getPassword(), encodedPassword)) {
            throw new MemberHandler(ErrorStatus.valueOf("MEMBER_NOT_FOUND"));
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
            signInMember.setInActiveDate(LocalDate.now());
            signInMember = memberRepository.save(signInMember);

            MemberResponseDTO.SignInResponseDTO signInMemberDTO =
                    MemberConverter.toSignInResponseDTO(signInMember);
            signInMemberDTO.setAccessToken(accessToken);

            return signInMemberDTO;
        } catch (AuthenticationException e) {
            throw new MemberHandler(ErrorStatus.valueOf("_UNAUTHORIZED"));
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
                            .orElseThrow(
                                    () ->
                                            new MemberHandler(
                                                    ErrorStatus.valueOf("MEMBER_NOT_FOUND")));
            final String savedToken = member.getRefreshToken();

            if (savedToken.matches(refreshToken)) {
                Authentication authentication = jwtTokenUtil.getAuthentication(accessToken);
                tokenInfo = jwtTokenUtil.generateToken(authentication, nickname);

                member.setRefreshToken(tokenInfo.getRefreshToken());
                memberRepository.save(member);
            }
        } else {
            throw new MemberHandler(ErrorStatus.valueOf("INVALID_TOKEN_ERROR"));
        }
        return tokenInfo;
    }
}
