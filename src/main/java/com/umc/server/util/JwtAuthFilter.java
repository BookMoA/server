package com.umc.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.domain.enums.TokenStatus;
import com.umc.server.service.MemberService.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final Optional<String> accessToken = extractToken(request);
            if (accessToken.isPresent()
                    && jwtTokenUtil.validateToken(accessToken.get())
                            == TokenStatus.valueOf("ACCESS")) {
                if (blacklistService.isBlacklisted(accessToken.get())) {
                    throw new MalformedJwtException("BlackList Token");
                }
                Authentication authentication = jwtTokenUtil.getAuthentication(accessToken.get());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new ExpiredJwtException(null, null, "Token expired");
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleExpiredJwtException(response, e);
        } catch (MalformedJwtException e) {
            handleBlackListJwtException(response, e);
        }
    }

    private void handleExpiredJwtException(HttpServletResponse response, ExpiredJwtException e)
            throws IOException {
        ApiResponse<Object> apiResponse =
                ApiResponse.onFailure(
                        ErrorStatus.EXPIRED_TOKEN_ERROR.getCode(),
                        ErrorStatus.EXPIRED_TOKEN_ERROR.getDescription(),
                        null);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    private void handleBlackListJwtException(HttpServletResponse response, MalformedJwtException e)
            throws IOException {
        ApiResponse<Object> apiResponse =
                ApiResponse.onFailure(
                        ErrorStatus._UNAUTHORIZED.getCode(),
                        ErrorStatus._UNAUTHORIZED.getDescription(),
                        null);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // 'Bearer '의 길이는 7자
            return Optional.of(token);
        }

        return Optional.empty();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // request 에서 요청 path 추출
        String path = request.getServletPath();

        // filter 에서 제외한 url 목록
        String[] excludedPaths = {"/users/auth/", "/swagger-ui/"};

        for (String excludedPath : excludedPaths) {
            if (path.startsWith(excludedPath)) {
                return true;
            }
        }

        return false;
    }
}
