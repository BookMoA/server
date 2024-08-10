package com.umc.server.repository;

import com.umc.server.domain.ClubPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
    Page<ClubPost> findByContextContaining(String word, PageRequest pageRequest);

    Page<ClubPost> findByTitleContaining(String word, PageRequest pageRequest);

    @Query("")
    Page<ClubPost> findByNicknameContaining(String word, PageRequest pageRequest);

    void deleteByClubId(Long clubId);
}
