package com.umc.server.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.domain.Member;
import com.umc.server.service.MemberService.KakaoService;
import com.umc.server.service.MemberService.MemberService;
import com.umc.server.service.MemberService.TokenBlacklistService;
import com.umc.server.web.dto.request.KakaoRequestDTO;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberRestController {

    @Value("${KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${KAKAO_REDIRECT_URL}")
    private String redirectUri;

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final TokenBlacklistService blacklistService;

    // TODO: 회원가입
    @Operation(
            summary = "일반 회원가입 API",
            description = "소셜 로그인이 아닌 일반 회원가입을 진행하는 API입니다. 정해진 서식에 맞는 값들을 입력해주세요.")
    @PostMapping("/auth/sign-up")
    public ApiResponse<MemberResponseDTO.SignInResponseDTO> signUp(
            @RequestBody MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {

        return ApiResponse.onSuccess(memberService.signUp(signUpRequestDTO));
    }

    // TODO: 일반 회원 로그인
    @Operation(
            summary = "일반 회원 로그인 API",
            description = "소셜로그인이 아닌 일반 회원가입을 진행한 사용자가 로그인을 진행하는 API입니다. 정해진 서식에 맞는 값들을 입력해주세요.")
    @PostMapping("/auth/sign-in")
    public ApiResponse<MemberResponseDTO.SignInResponseDTO> signIn(
            @RequestBody MemberRequestDTO.SignInRequestDTO signInRequestDTO) {

        return ApiResponse.onSuccess(memberService.signIn(signInRequestDTO));
    }

    // TODO: 소셜 로그인 구현
    @Operation(summary = "카카오 소셜 로그인 API", description = "소셜 로그인을 진행하는 API입니다.")
    @GetMapping("/auth/kakao/sign-in")
    public RedirectView kakaoSignIn() {
        RedirectView redirectView = new RedirectView();
        String url =
                "https://kauth.kakao.com/oauth/authorize?"
                        + "client_id="
                        + clientId
                        + "&redirect_uri="
                        + redirectUri
                        + "&response_type=code";
        redirectView.setUrl(url);
        return redirectView;
    }

    @Operation(hidden = true)
    @GetMapping("/auth/kakao/callback")
    public ApiResponse<MemberResponseDTO.SignInResponseDTO> getKakaoToken(
            @RequestParam("code") String code,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            @RequestParam(value = "state", required = false) String state)
            throws JsonProcessingException {

        if (error != null && !error.isEmpty()) {
            throw new MemberHandler(ErrorStatus.KAKAO_SIGN_IN_ERROR);
        }

        String accessToken = kakaoService.getAccessToken(code);
        KakaoRequestDTO.SignUpRequestDTO kakaoSignUp = kakaoService.getMemberInfo(accessToken);

        return ApiResponse.onSuccess(kakaoService.signUp(kakaoSignUp, accessToken));
    }

    // TODO: 토큰 재발급 후 로그인
    @Operation(
            summary = "token 만료시 재로그인 api",
            description = "access token이 만료된 경우 token 재 발급 후 로그인 하는 api입니다.")
    @PostMapping("/auth/renew")
    public ApiResponse<MemberResponseDTO.TokenInfo> reSignIn(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody MemberRequestDTO.RequestTokenDTO requestToken) {

        String refreshToken = requestToken.getRefreshToken();
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new MemberHandler(ErrorStatus._BAD_REQUEST);
        }
        String accessToken = authorizationHeader.substring(7);
        MemberResponseDTO.TokenInfo tokenInfo = memberService.renew(accessToken, refreshToken);
        return ApiResponse.onSuccess(tokenInfo);
    }

    // TODO: 로그아웃
    @Operation(
            summary = "모든 회원 대상 로그아웃 api",
            description = "일반, 소셜 로그인 회원 모두가 이 api를 통해 로그아웃할 수 있습니다.")
    @GetMapping("/sign-out")
    public ApiResponse<String> signOut(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new MemberHandler(ErrorStatus._BAD_REQUEST);
        }
        String accessToken = authorizationHeader.substring(7);
        memberService.signOut(signInmember);
        blacklistService.addToBlacklist(accessToken);

        return ApiResponse.onSuccess("로그아웃에 성공하였습니다.");
    }
}
