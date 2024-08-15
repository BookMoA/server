package com.umc.server.repository;

import com.umc.server.domain.mapping.ClubPostComment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPostCommentRepository extends JpaRepository<ClubPostComment, Long> {
    Slice<ClubPostComment> findAllByClubPostId(Long postId, PageRequest pageRequest);
}
