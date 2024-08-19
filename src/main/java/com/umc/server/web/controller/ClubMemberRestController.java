package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubMemberConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.service.ClubMemberService.ClubMemberService;
import com.umc.server.web.dto.request.ClubMemberRequestDTO;
import com.umc.server.web.dto.response.ClubMemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/members")
@RequiredArgsConstructor
public class ClubMemberRestController {
    private final ClubMemberService clubMemberService;

    @Operation(summary = "독서 모임 멤버 생성 (가입) API", description = "독서 모임 멤버 생성 (가입) API")
    @PostMapping("")
    public ApiResponse<ClubMemberResponseDTO.ClubMemberCreateResponseDTO> clubMemberCreateAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubMemberRequestDTO.ClubMemberCreateRequestDTO request) {
        ClubMember clubMember = clubMemberService.createClubMember(signInmember, request);
        return ApiResponse.onSuccess(ClubMemberConverter.toClubMemberCreateResponseDTO(clubMember));
    }

    @Operation(summary = "독서 모임 멤버 조회 API", description = "독서 모임 멤버 조회 API")
    @GetMapping("")
    @Parameter(name = "clubId", description = "조회할 독서 모임의 아이디")
    public ApiResponse<List<ClubMemberResponseDTO.ClubMemberDetailResponseDTO>> clubMemberListAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestParam(name = "clubId", defaultValue = "") Long clubId) {
        List<ClubMember> clubMembers = clubMemberService.readClubMember(signInmember, clubId);
        return ApiResponse.onSuccess(ClubMemberConverter.toClubMemberListResponseDTO(clubMembers));
    }

    @Operation(summary = "독서 모임 멤버 수정 (상메) API", description = "독서 모임 멤버 수정 (상메) API")
    @PatchMapping("")
    public ApiResponse<ClubMemberResponseDTO.ClubMemberDetailResponseDTO> clubMemberUpdateAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubMemberRequestDTO.ClubMemberUpdateRequestDTO request) {
        ClubMember clubMember = clubMemberService.updateClubMember(signInmember, request);
        return ApiResponse.onSuccess(ClubMemberConverter.toClubMemberDetailResponseDTO(clubMember));
    }

    @Operation(summary = "모임원 강퇴 API", description = "독서 모임 멤버 강퇴 API (모임장 권한)")
    @DeleteMapping("/drop")
    public ApiResponse<String> clubMemberDropAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubMemberRequestDTO.ClubMemberDropRequestDTO request) {
        clubMemberService.dropClubMember(signInmember, request);
        return ApiResponse.onSuccess("멤버 아이디" + request.getMemberId() + "을 모임에서 강퇴했습니다.");
    }

    @Operation(summary = "모임원 탈퇴 API", description = "독서 모임 탈퇴 API (모임원)")
    @DeleteMapping("")
    public ApiResponse<String> clubMemberDeleteAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        clubMemberService.deleteClubMember(signInmember);
        return ApiResponse.onSuccess(signInmember.getNickname() + "가 소속된 독서 모임에서 탈퇴했습니다.");
    }

    @Operation(summary = "모임 리더 탈퇴 API", description = "독서 모임 탈퇴 API (모임장)")
    @DeleteMapping("/reader")
    public ApiResponse<String> clubMemberDeleteReaderAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubMemberRequestDTO.ClubMemberDeleteReaderRequestDTO request) {
        clubMemberService.deleteReaderClubMember(signInmember, request);
        return ApiResponse.onSuccess(signInmember.getNickname() + "가 소속된 독서 모임에서 탈퇴했습니다.(모임장)");
    }
}
