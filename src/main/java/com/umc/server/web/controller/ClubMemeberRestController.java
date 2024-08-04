package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubMemberConverter;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.service.ClubMemberService.ClubMemberService;
import com.umc.server.web.dto.request.ClubMemberRequestDTO;
import com.umc.server.web.dto.response.ClubMemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/members")
@RequiredArgsConstructor
public class ClubMemeberRestController {
    private final ClubMemberService clubMemberService;

    @Operation(summary = "독서 모임 멤버 생성 (가입) API", description = "독서 모임 멤버 생성 (가입) API")
    @PostMapping("/{memberId}")
    @Parameter(name = "memberId", description = "사용자의 아이디(토큰 적용 전까지 임시)")
    public ApiResponse<ClubMemberResponseDTO.ClubMemberCreateResponseDTO> clubMemberCreateAPI(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid ClubMemberRequestDTO.ClubMemberCreateRequestDTO request) {
        ClubMember clubMember = clubMemberService.createClubMember(memberId, request);
        return ApiResponse.onSuccess(ClubMemberConverter.toClubMemberCreateResponseDTO(clubMember));
    }

    @Operation(summary = "독서 모임 멤버 조회 API", description = "독서 모임 멤버 조회 API")
    @GetMapping("/{memberId}")
    @Parameter(name = "memberId", description = "사용자의 아이디(토큰 적용 전까지 임시)")
    @Parameter(name = "clubId", description = "조회할 독서 모임의 아이디")
    public ApiResponse<List<ClubMemberResponseDTO.ClubMemberDetailResponseDTO>> clubMemberListAPI(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(name = "clubId", defaultValue = "") Long clubId) {
        List<ClubMember> clubMembers = clubMemberService.readClubMember(memberId, clubId);
        return ApiResponse.onSuccess(ClubMemberConverter.toClubMemberListResponseDTO(clubMembers));
    }

    @Operation(summary = "독서 모임 멤버 수정 (상메) API", description = "독서 모임 멤버 수정 (상메) API")
    @PatchMapping("/{memberId}")
    @Parameter(name = "memberId", description = "사용자의 아이디(토큰 적용 전까지 임시)")
    public ApiResponse<ClubMemberResponseDTO.ClubMemberDetailResponseDTO> clubMemberUpdateAPI(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody @Valid ClubMemberRequestDTO.ClubMemberUpdateRequestDTO request) {
        ClubMember clubMember = clubMemberService.updateClubMember(memberId, request);
        return ApiResponse.onSuccess(ClubMemberConverter.toClubMemberDetailResponseDTO(clubMember));
    }
}
