package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubPostLikeConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubPostLike;
import com.umc.server.service.ClubPostLikeService.ClubPostLikeService;
import com.umc.server.web.dto.request.ClubPostLikeRequestDTO;
import com.umc.server.web.dto.response.ClubPostLikeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/posts/likes")
@RequiredArgsConstructor
public class ClubPostLikeRestController {
    private final ClubPostLikeService clubPostLikeService;

    @Operation(summary = "독서 모임 게시글에 공감 API", description = "독서 모임 게시글에 공감 API")
    @PostMapping("")
    public ApiResponse<ClubPostLikeResponseDTO.ClubPostLikeCreateResponseDTO>
            clubPostCommentCreateAPI(
                    @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
                    @RequestBody @Valid
                            ClubPostLikeRequestDTO.ClubPostLikeCreateRequestDTO request) {
        ClubPostLike clubPostLike = clubPostLikeService.createClubPostLike(signInmember, request);
        return ApiResponse.onSuccess(
                ClubPostLikeConverter.toClubPostLikeCreateResponseDTO(clubPostLike));
    }

    @Operation(summary = "독서 모임 게시글에 공감 취소 API", description = "독서 모임 게시글에 공감 취소 API")
    @DeleteMapping("")
    public ApiResponse<String> clubPostCommentDeleteAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubPostLikeRequestDTO.ClubPostLikeDeleteRequestDTO request) {
        clubPostLikeService.deleteClubPostLike(signInmember, request);
        return ApiResponse.onSuccess(request.getPostId() + " 의 공감을 취소했습니다.");
    }
}
