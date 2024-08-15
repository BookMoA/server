package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubPostConverter;
import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.service.ClubPostService.ClubPostService;
import com.umc.server.web.dto.request.ClubPostRequestDTO;
import com.umc.server.web.dto.response.ClubPostResponseDTO;
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
@RequestMapping("/clubs/posts")
@RequiredArgsConstructor
public class ClubPostRestController {
    private final ClubPostService clubPostService;

    @Operation(summary = "독서 모임 게시글 생성 API", description = "독서 모임 게시글 생성 API")
    @PostMapping("")
    public ApiResponse<ClubPostResponseDTO.ClubPostCreateResponseDTO> clubPostCreateAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubPostRequestDTO.ClubPostCreateRequestDTO request) {
        ClubPost clubPost = clubPostService.createClubPost(signInmember, request);
        return ApiResponse.onSuccess(ClubPostConverter.toClubPostCreateResponseDTO(clubPost));
    }

    @Operation(summary = "독서 모임 게시글 조회 API", description = "독서 모임 게시글 조회 API")
    @GetMapping("")
    @Parameter(name = "postId", description = "조회할 게시글의 아이디")
    public ApiResponse<ClubPostResponseDTO.ClubPostDetailResponseDTO> clubPostDetailAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestParam(name = "postId", defaultValue = "") Long postId) {
        Optional<ClubPost> clubPost = clubPostService.readClubPost(signInmember, postId);
        return ApiResponse.onSuccess(ClubPostConverter.toClubPostDetailResponseDTO(clubPost));
    }

    @Operation(summary = "독서 모임 게시글 리스트 조회 API", description = "독서 모임 게시글 리스트 조회 API")
    @GetMapping("/list")
    @Parameter(name = "clubId", description = "조회할 독서 모임의 아이디")
    @Parameter(name = "page", description = "페이징")
    public ApiResponse<ClubPostResponseDTO.ClubPostListResponseDTO> clubPostListAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestParam(name = "clubId", defaultValue = "") Long clubId,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Slice<ClubPost> clubPost = clubPostService.readClubPostList(signInmember, clubId, page);
        return ApiResponse.onSuccess(
                ClubPostConverter.toClubPostListResponseDTO(clubId, page, clubPost));
    }

    @Operation(summary = "독서 모임 게시글 검색 API", description = "독서 모임 게시글 검색 API")
    @GetMapping("/search")
    @Parameter(name = "category", description = "검색 분류(글-context, 제목-title, 글쓴이-writer)")
    @Parameter(name = "word", description = "검색어")
    @Parameter(name = "page", description = "페이징")
    public ApiResponse<ClubPostResponseDTO.ClubPostSearchResponseDTO> clubPostSearchAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestParam(name = "category", defaultValue = "context") String category,
            @RequestParam(name = "word", defaultValue = "") String word,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<ClubPost> clubPost =
                clubPostService.searchClubPost(signInmember, category, word, page);
        return ApiResponse.onSuccess(
                ClubPostConverter.toClubPostSearchResponseDTO(category, word, page, clubPost));
    }

    @Operation(summary = "독서 모임 게시글 수정 API", description = "독서 모임 게시글 수정 API")
    @PatchMapping("")
    public ApiResponse<ClubPostResponseDTO.ClubPostDetailResponseDTO> clubPostUpdateAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubPostRequestDTO.ClubPostUpdateRequestDTO request) {
        ClubPost clubPost = clubPostService.updateClubPost(signInmember, request);
        return ApiResponse.onSuccess(
                ClubPostConverter.toClubPostDetailResponseDTO(Optional.ofNullable(clubPost)));
    }

    @Operation(summary = "독서 모임 게시글 삭제 API", description = "독서 모임 게시글 삭제 API")
    @DeleteMapping("")
    public ApiResponse<String> clubPostDeleteAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubPostRequestDTO.ClubPostDeleteRequestDTO request) {
        clubPostService.deleteClubPost(signInmember, request);
        return ApiResponse.onSuccess(request.getPostId() + " 게시물 삭제했습니다.");
    }
}
