package com.umc.server.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.service.MemberService.KakaoService;
import com.umc.server.service.MemberService.MemberService;
import com.umc.server.web.dto.request.KakaoRequsetDTO;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    // TODO: 회원가입
    @Operation(
            summary = "일반 회원가입 API",
            description = "소셜 로그인이 아닌 일반 회원가입을 진행하는 API입니다. 정해진 서식에 맞는 값들을 입력해주세요.")
    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(
            @RequestBody MemberRequestDTO.SignUpRequestDTO signUpRequestDTO) {

        memberService.signUp(signUpRequestDTO);
        return ApiResponse.onSuccess("회원가입에 성공하였습니다.");
    }

    // TODO: 일반 회원 로그인
    @Operation(
            summary = "일반 회원 로그인 API",
            description = "소셜로그인이 아닌 일반 회원가입을 진행한 사용자가 로그인을 진행하는 API입니다. 정해진 서식에 맞는 값들을 입력해주세요.")
    @PostMapping("/sign-in")
    public ApiResponse<MemberResponseDTO.SignInResponseDTO> signIn(
            @RequestBody MemberRequestDTO.SignInRequestDTO signInRequestDTO) {

        final Member signInMember = memberService.signIn(signInRequestDTO);
        return ApiResponse.onSuccess(MemberConverter.toSignInResponseDTO(signInMember));
    }

    // TODO: 소셜 로그인 구현
    @Operation(summary = "카카오 소셜 로그인 API", description = "소셜 로그인을 진행하는 API입니다.")
    @GetMapping("/kakao/sign-in")
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
    @GetMapping("/kakao/callback")
    public ApiResponse<MemberResponseDTO.SignInResponseDTO> getKakaoToken(
            @RequestParam("code") String code,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            @RequestParam(value = "state", required = false) String state)
            throws JsonProcessingException {

        if (error != null && !error.isEmpty()) {
            throw new MemberHandler(ErrorStatus.valueOf("KAKAO_SIGN_IN_ERROR"));
        }

        String accessToken = kakaoService.getAccessToken(code);
        KakaoRequsetDTO.SignUpRequestDTO kakaoSignUp = kakaoService.getMemberInfo(accessToken);
        final Member kakoSignInMember = kakaoService.signUp(kakaoSignUp);
        return ApiResponse.onSuccess(MemberConverter.toSignInResponseDTO(kakoSignInMember));
    }
}
