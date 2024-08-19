package com.umc.server.repository;

import com.umc.server.domain.ClubPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
    Page<ClubPost> findByContextContaining(String word, PageRequest pageRequest);

    Page<ClubPost> findByTitleContaining(String word, PageRequest pageRequest);

    @Query("SELECT p FROM ClubPost p WHERE p.member.nickname = :nickname ORDER BY p.createdAt DESC")
    Page<ClubPost> findByNicknameContaining(
            @Param("nickname") String nickname, PageRequest pageRequest);

    void deleteByClubId(Long clubId);
}
