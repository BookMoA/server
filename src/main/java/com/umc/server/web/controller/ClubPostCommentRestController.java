package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.converter.ClubPostCommentConverter;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubPostComment;
import com.umc.server.service.ClubPostCommentService.ClubPostCommentService;
import com.umc.server.web.dto.request.ClubPostCommentRequestDTO;
import com.umc.server.web.dto.response.ClubPostCommentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/posts/comments")
@RequiredArgsConstructor
public class ClubPostCommentRestController {
    private final ClubPostCommentService clubPostCommentService;

    @Operation(summary = "독서 모임 게시글에 댓글 생성 API", description = "독서 모임 게시글에 댓글 생성 API")
    @PostMapping("")
    public ApiResponse<ClubPostCommentResponseDTO.ClubPostCommentCreateResponseDTO>
            clubPostCommentCreateAPI(
                    @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
                    @RequestBody @Valid
                            ClubPostCommentRequestDTO.ClubPostCommentCreateRequestDTO request) {
        ClubPostComment clubPostComment =
                clubPostCommentService.createClubPostComment(signInmember, request);
        return ApiResponse.onSuccess(
                ClubPostCommentConverter.toClubPostCommentCreateResponseDTO(clubPostComment));
    }

    @Operation(summary = "독서 모임 게시글의 댓글 리스트 조회 API", description = "독서 모임 게시글의 댓글 리스트 조회 API")
    @GetMapping("")
    @Parameter(name = "postId", description = "조회할 게시글의 아이디")
    @Parameter(name = "page", description = "페이징")
    public ApiResponse<ClubPostCommentResponseDTO.ClubPostCommentListResponseDTO>
            clubPostCommentListAPI(
                    @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
                    @RequestParam(name = "postId", defaultValue = "") Long postId,
                    @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Slice<ClubPostComment> clubPostComment =
                clubPostCommentService.readClubPostCommentList(signInmember, postId, page);
        return ApiResponse.onSuccess(
                ClubPostCommentConverter.toClubPostCommentListResponseDTO(
                        postId, page, clubPostComment));
    }

    @Operation(summary = "독서 모임 게시글 삭제 API", description = "독서 모임 게시글 삭제 API")
    @DeleteMapping("")
    public ApiResponse<String> clubPostCommentDeleteAPI(
            @Parameter(hidden = true) @AuthenticationPrincipal Member signInmember,
            @RequestBody @Valid ClubPostCommentRequestDTO.ClubPostCommentDeleteRequestDTO request) {
        clubPostCommentService.deleteClubPostComment(signInmember, request);
        return ApiResponse.onSuccess(request.getPostCommentId() + " 댓글을 삭제했습니다.");
    }
}
