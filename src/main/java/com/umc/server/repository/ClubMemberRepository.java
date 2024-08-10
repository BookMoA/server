package com.umc.server.repository;

import com.umc.server.domain.mapping.ClubMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByMemberId(Long memberId);

    List<ClubMember> findAllByClubId(Long clubId);

    void deleteByClubId(Long memberId);
}
