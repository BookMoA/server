package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubConverter;
import com.umc.server.domain.Club;
import com.umc.server.service.ClubService.ClubService;
import com.umc.server.web.dto.request.ClubRequestDTO;
import com.umc.server.web.dto.response.ClubResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubRestController {

    private final ClubService clubService;

    @Operation(summary = "Book Club create API", description = "독서 모임을 생성 API")
    @PostMapping("")
    public ApiResponse<ClubResponseDTO.ClubCreateResponseDTO> clubCreateAPI(
            @RequestBody @Valid ClubRequestDTO.ClubCreateRequestDTO request) {
        Club club = clubService.createClub(request);
        return ApiResponse.onSuccess(ClubConverter.toClubCreateResponseDTO(club));
    }

    @Operation(summary = "My Book Club read API", description = "사용자가 속해있는 독서 모임 조회 API")
    @GetMapping("/{memberId}")
    @Parameter(name = "memberId", description = "사용자의 아이디")
    public ApiResponse<ClubResponseDTO.MyClubResponseDTO> myClubAPI(
            @PathVariable(name = "memberId") Long memberId) {
        Optional<Club> club = clubService.readMyClub(memberId);
        return ApiResponse.onSuccess(ClubConverter.toMyClubResponseDTO(club));
    }

    @Operation(summary = "Book Club read API", description = "특정 독서 모임의 상세 정보 조회 API")
    @GetMapping("")
    @Parameter(name = "clubId", description = "조회할 독서 모임의 아이디")
    public ApiResponse<ClubResponseDTO.ClubDetailResponseDTO> clubDetailAPI(
            @RequestParam(name = "clubId", defaultValue = "") Long clubId) {
        Club club = clubService.readClubDetail(clubId);
        return ApiResponse.onSuccess(ClubConverter.toClubDetailResponseDTO(club));
    }

    @Operation(summary = "Book Club update API", description = "독서 모임의 정보(한 줄 소개, 공지사항) 수정 API")
    @PatchMapping("/{clubId}")
    @Parameter(name = "clubId", description = "수정할 모임의 아이디")
    public ApiResponse<ClubResponseDTO.ClubUpdateResponseDTO> clubUpdateAPI(
            @PathVariable(name = "clubId") Long clubId,
            @RequestBody @Valid ClubRequestDTO.ClubUpdateRequestDTO request) {
        Club club = clubService.updateClub(clubId, request);
        return ApiResponse.onSuccess(ClubConverter.toClubUpdateResponseDTO(club));
    }

    @Operation(summary = "Book Club delete API", description = "독서 모임 삭제 API")
    @DeleteMapping("")
    public void clubDeleteAPI(@RequestBody @Valid ClubRequestDTO.ClubDeleteRequestDTO request) {
        clubService.deleteClub(request.getClubId(), request.getMemberId());
    }
}
