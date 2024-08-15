package com.umc.server.repository;

import com.umc.server.domain.mapping.ClubPostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPostLikeRepository extends JpaRepository<ClubPostLike, Long> {
    Optional<ClubPostLike> findByClubPostIdAndMemberId(Long postId, Long memberId);
}
