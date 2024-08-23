package com.umc.server.converter;

import com.umc.server.domain.Club;
import com.umc.server.domain.ClubPost;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.ClubPostRequestDTO;
import com.umc.server.web.dto.response.ClubPostResponseDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class ClubPostConverter {

    public static ClubPost toClubPost(
            Member member, Club club, ClubPostRequestDTO.ClubPostCreateRequestDTO request) {
        return ClubPost.builder()
                .member(member)
                .club(club)
                .title(request.getTitle())
                .context(request.getContext())
                .build();
    }

    public static ClubPostResponseDTO.ClubPostCreateResponseDTO toClubPostCreateResponseDTO(
            ClubPost clubPost) {
        return ClubPostResponseDTO.ClubPostCreateResponseDTO.builder()
                .postId(clubPost.getId())
                .build();
    }

    public static ClubPostResponseDTO.ClubPostDetailResponseDTO toClubPostDetailResponseDTO(
            Optional<ClubPost> clubPost) {

        if (clubPost.isPresent()) {
            ClubPost post = clubPost.get();

            return ClubPostResponseDTO.ClubPostDetailResponseDTO.builder()
                    .postId(post.getId())
                    .memberId(post.getMember().getId())
                    .title(post.getTitle())
                    .nickname(post.getMember().getNickname())
                    .context(post.getContext())
                    .commentCount(post.getClubPostCommentList().size())
                    .likeCount(post.getClubPostLikeList().size())
                    .createAt(post.getCreatedAt())
                    .updateAt(post.getUpdatedAt())
                    .build();
        } else {
            return ClubPostResponseDTO.ClubPostDetailResponseDTO.builder().build();
        }
    }

    public static ClubPostResponseDTO.ClubPostListResponseDTO toClubPostListResponseDTO(
            Long clubId, Integer page, Slice<ClubPost> postSlice) {
        List<ClubPostResponseDTO.ClubPostDetailResponseDTO> postList = new ArrayList<>();
        for (ClubPost post : postSlice.getContent()) {
            postList.add(toClubPostDetailResponseDTO(Optional.ofNullable(post)));
        }
        return ClubPostResponseDTO.ClubPostListResponseDTO.builder()
                .clubId(clubId)
                .page(page)
                .size(postSlice.getSize())
                .postList(postList)
                .build();
    }

    public static ClubPostResponseDTO.ClubPostSearchResponseDTO toClubPostSearchResponseDTO(
            Long clubId, String category, String word, Integer page, Page<ClubPost> postPage) {
        List<ClubPostResponseDTO.ClubPostDetailResponseDTO> postList = new ArrayList<>();
        for (ClubPost post : postPage.getContent()) {
            postList.add(toClubPostDetailResponseDTO(Optional.ofNullable(post)));
        }
        return ClubPostResponseDTO.ClubPostSearchResponseDTO.builder()
                .clubId(clubId)
                .category(category)
                .word(word)
                .page(page)
                .totalElements((int) postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .size(postPage.getSize())
                .postList(postList)
                .build();
    }
}
