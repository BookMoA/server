package com.umc.server.repository;

import com.umc.server.domain.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
    void deleteByClubId(Long clubId);
}
