package com.umc.server.web.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClubPostCommentResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCommentCreateResponseDTO {
        Long postCommentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCommentDetailResponseDTO {
        Long commentId;

        Long memberId;

        String context;

        LocalDateTime createAt;

        LocalDateTime updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubPostCommentListResponseDTO {
        Long postId;

        Integer page;

        Integer size;

        @Builder.Default List<ClubPostCommentDetailResponseDTO> commentList = new ArrayList<>();
    }
}
