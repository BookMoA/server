package com.umc.server.converter;

import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.ClubPostComment;
import com.umc.server.web.dto.request.ClubPostCommentRequestDTO;
import com.umc.server.web.dto.response.ClubPostCommentResponseDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Slice;

public class ClubPostCommentConverter {
    public static ClubPostComment toClubPostComment(
            Member member,
            ClubPost clubPost,
            ClubPostCommentRequestDTO.ClubPostCommentCreateRequestDTO request) {
        return ClubPostComment.builder()
                .member(member)
                .clubPost(clubPost)
                .context(request.getContext())
                .build();
    }

    public static ClubPostCommentResponseDTO.ClubPostCommentCreateResponseDTO
            toClubPostCommentCreateResponseDTO(ClubPostComment clubPostComment) {
        return ClubPostCommentResponseDTO.ClubPostCommentCreateResponseDTO.builder()
                .postCommentId(clubPostComment.getId())
                .build();
    }

    public static ClubPostCommentResponseDTO.ClubPostCommentDetailResponseDTO
            toClubPostCommentDetailResponseDTO(ClubPostComment clubPostComment) {

        return ClubPostCommentResponseDTO.ClubPostCommentDetailResponseDTO.builder()
                .commentId(clubPostComment.getId())
                .memberId(clubPostComment.getMember().getId())
                .context(clubPostComment.getContext())
                .createAt(clubPostComment.getCreatedAt())
                .updateAt(clubPostComment.getUpdatedAt())
                .build();
    }

    public static ClubPostCommentResponseDTO.ClubPostCommentListResponseDTO
            toClubPostCommentListResponseDTO(
                    Long postId, Integer page, Slice<ClubPostComment> commentSlice) {
        List<ClubPostCommentResponseDTO.ClubPostCommentDetailResponseDTO> commentList =
                new ArrayList<>();
        for (ClubPostComment comment : commentSlice.getContent()) {
            commentList.add(toClubPostCommentDetailResponseDTO(comment));
        }
        return ClubPostCommentResponseDTO.ClubPostCommentListResponseDTO.builder()
                .postId(postId)
                .page(page)
                .size(commentSlice.getSize())
                .commentList(commentList)
                .build();
    }
}
