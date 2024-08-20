package com.umc.server.repository;

import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    Optional<MemberBook> findByIdAndMember(Long id, Member member);

    @Query(
            "SELECT mb.book.id "
                    + "FROM MemberBook mb "
                    + "GROUP BY mb.book.id "
                    + "ORDER BY AVG(mb.score) DESC")
    List<Long> findTopBooksByAverageScore(Pageable pageable);

    //    Boolean existsByMember(Member member);

    Optional<List<MemberBook>> findAllByMember(Member member);
}
