package com.umc.server.repository;

import com.umc.server.domain.Club;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String name);

    Optional<Club> findByCode(String code);

    @Query(
            "SELECT c FROM Club c LEFT JOIN c.clubMemberList cm GROUP BY c.id HAVING COUNT(cm) <= 20 ORDER BY COUNT(cm) DESC")
    Slice<Club> findAllOrderByClubMemberCountDesc(PageRequest pageRequest);

    @Query("SELECT c FROM Club c LEFT JOIN c.clubPostList cp GROUP BY c ORDER BY COUNT(cp) DESC")
    Slice<Club> findAllOrderByClubPostCountDesc(PageRequest pageRequest);

    Page<Club> findByNameContaining(String word, PageRequest pageRequest);

    Page<Club> findByNoticeContaining(String word, PageRequest pageRequest);
}
