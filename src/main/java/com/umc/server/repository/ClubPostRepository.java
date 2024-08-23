package com.umc.server.repository;

import com.umc.server.domain.ClubPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

    Slice<ClubPost> findByClubId(Long clubId, PageRequest pageRequest);

    Page<ClubPost> findByClubIdAndContextContaining(
            Long clubId, String word, PageRequest pageRequest);

    Page<ClubPost> findByClubIdAndTitleContaining(
            Long clubId, String word, PageRequest pageRequest);

    //    @Query("SELECT p FROM ClubPost p WHERE p.club.id = :clubId and p.member.nickname =
    // :nickname ORDER BY p.createdAt DESC")
    Page<ClubPost> findByClubIdAndMemberNicknameContaining(
            Long clubId, String nickname, PageRequest pageRequest);
}
