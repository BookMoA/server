package com.umc.server.repository;

import com.umc.server.domain.BookMemo;
import com.umc.server.domain.mapping.MemberBook;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookMemoRepository extends JpaRepository<BookMemo, Long> {

    @Query(
            "SELECT bm FROM BookMemo bm JOIN bm.memberBook mb JOIN mb.member m WHERE bm.body LIKE %:keyword% AND m.id = :memberId")
    Page<BookMemo> findByKeywordAndMemberId(
            @Param("keyword") String keyword, @Param("memberId") Long memberId, Pageable pageable);

    Optional<BookMemo> findByIdAndMemberBook(Long id, MemberBook memberBook);

    Page<BookMemo> findAllByMemberBook(MemberBook memberBook, PageRequest pageRequest);
}
