package com.umc.server.repository;

import com.umc.server.domain.mapping.ClubMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByMemberId(Long memberId);

    void deleteByClubId(Long clubId);
}
