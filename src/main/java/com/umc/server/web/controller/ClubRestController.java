package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubConverter;
import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.service.ClubService.ClubService;
import com.umc.server.web.dto.request.ClubRequestDTO;
import com.umc.server.web.dto.response.ClubResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubRestController {

    private final ClubService clubService;

    @Operation(summary = "모임 생성 API", description = "독서 모임을 생성 API")
    @PostMapping("")
    public ApiResponse<ClubResponseDTO.ClubCreateResponseDTO> clubCreateAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubRequestDTO.ClubCreateRequestDTO request) {
        Club club = clubService.createClub(signInmember, request);
        return ApiResponse.onSuccess(ClubConverter.toClubCreateResponseDTO(club));
    }

    @Operation(summary = "내 모임 조회 API", description = "사용자가 속해있는 독서 모임 조회 API")
    @GetMapping("")
    public ApiResponse<ClubResponseDTO.MyClubResponseDTO> myClubAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember) {
        Optional<Club> club = clubService.readMyClub(signInmember);
        return ApiResponse.onSuccess(ClubConverter.toMyClubResponseDTO(signInmember, club));
    }

    @Operation(summary = "모임 상세 조회 API", description = "특정 독서 모임의 상세 정보 조회 API")
    @GetMapping("/detail")
    @Parameter(name = "clubId", description = "조회할 독서 모임의 아이디")
    public ApiResponse<ClubResponseDTO.ClubDetailResponseDTO> clubDetailAPI(
            @RequestParam(name = "clubId", defaultValue = "") Long clubId) {
        Club club = clubService.readClubDetail(clubId);
        return ApiResponse.onSuccess(ClubConverter.toClubDetailResponseDTO(club));
    }

    @Operation(summary = "모임 수정 API", description = "독서 모임의 정보(한 줄 소개, 공지사항) 수정 API")
    @PatchMapping("")
    public ApiResponse<ClubResponseDTO.ClubUpdateResponseDTO> clubUpdateAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubRequestDTO.ClubUpdateRequestDTO request) {
        Club club = clubService.updateClub(signInmember, request);
        return ApiResponse.onSuccess(ClubConverter.toClubUpdateResponseDTO(club));
    }

    @Operation(summary = "모임 삭제 API", description = "독서 모임 삭제 API")
    @DeleteMapping("")
    public void clubDeleteAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubRequestDTO.ClubDeleteRequestDTO request) {
        clubService.deleteClub(signInmember, request);
    }

    @Operation(summary = "모임 추천 API", description = "추천 독서 모임의 리스트 조회 API")
    @GetMapping("/recommend")
    @Parameter(name = "category", description = "추천 분류 (new,activity,deadline)")
    public ApiResponse<ClubResponseDTO.ClubRecommendResponseDTO> clubRecommendAPI(
            @RequestParam(name = "category", defaultValue = "new") String category,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Slice<Club> clubSlice = clubService.recommendClub(category, page);
        return ApiResponse.onSuccess(
                ClubConverter.toClubRecommendResponseDTO(clubSlice, category, page));
    }

    @Operation(summary = "모임 검색 API", description = "검색한 독서 모임의 리스트 조회 API")
    @GetMapping("/search")
    @Parameter(name = "category", description = "검색 분류 (name, notice)")
    @Parameter(name = "word", description = "검색어")
    public ApiResponse<ClubResponseDTO.ClubSearchResponseDTO> clubSearchAPI(
            @RequestParam(name = "category", defaultValue = "name") String category,
            @RequestParam(name = "word", defaultValue = "") String word,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<Club> clubPage = clubService.searchClub(category, word, page);
        return ApiResponse.onSuccess(
                ClubConverter.toClubSearchResponseDTO(clubPage, category, word, page));
    }
}
