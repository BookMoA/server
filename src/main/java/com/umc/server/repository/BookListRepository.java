package com.umc.server.repository;

import com.umc.server.domain.BookList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookListRepository extends JpaRepository<BookList, Long> {
    // 멤버 ID와 저장된 책 리스트를 페이지네이션과 함께 조회 -> 보관함
    @Query(
            "SELECT bl FROM BookList bl WHERE bl.member.id = :memberId "
                    + "OR EXISTS (SELECT 1 FROM MemberBookList mbl WHERE mbl.bookList.id = bl.id AND mbl.isStored = true AND mbl.member.id = :memberId)")
    Page<BookList> findStoredBooksByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT b FROM BookList b WHERE b.title LIKE %:title% AND b.listStatus = 'PUBLIC'")
    Page<BookList> findByTitleContaining(@Param("title") String title, Pageable pageable);
}
