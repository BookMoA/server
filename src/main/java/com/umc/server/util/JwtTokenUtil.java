package com.umc.server.util;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.domain.enums.TokenStatus;
import com.umc.server.service.MemberService.CustomUserDetailsService;
import com.umc.server.web.dto.response.MemberResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil {

    private final Key key;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenUtil(
            @Value("${JWT_SECRET}") String secretKey,
            CustomUserDetailsService customUserDetailsService) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes());
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        this.customUserDetailsService = customUserDetailsService;
    }

    // TODO: 토큰 생성
    public MemberResponseDTO.TokenInfo generateToken(
            Authentication authentication, String nickname) {
        final String authorities =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        final LocalDateTime now = LocalDateTime.now();

        final String accessToken =
                Jwts.builder()
                        .setSubject(authentication.getName())
                        .claim("auth", authorities)
                        .claim("nickname", nickname)
                        .setExpiration(
                                Date.from(
                                        now.plusHours(2)
                                                .atZone(ZoneId.systemDefault())
                                                .toInstant()))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        final String refreshToken =
                Jwts.builder()
                        .setExpiration(
                                Date.from(
                                        now.plusDays(3).atZone(ZoneId.systemDefault()).toInstant()))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        return MemberResponseDTO.TokenInfo.of(accessToken, refreshToken);
    }

    // TODO: 토큰 유효 검증
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return TokenStatus.valueOf("ACCESS");
        } catch (ExpiredJwtException e) {
            return TokenStatus.valueOf("EXPIRED");
        } catch (Exception e) {
            return TokenStatus.valueOf("INVALID");
        }
    }

    // TODO: 토큰 복호화
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // TODO: 로그인 유저 확인
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new MemberHandler(ErrorStatus.valueOf("_UNAUTHORIZED"));
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken);
    }
}
