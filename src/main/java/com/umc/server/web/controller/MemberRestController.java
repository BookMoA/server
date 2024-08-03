package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.MemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.service.MemberService.MemberService;
import com.umc.server.web.dto.request.MemberRequestDTO;
import com.umc.server.web.dto.response.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberRestController {

    private final MemberService memberService;

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
}
