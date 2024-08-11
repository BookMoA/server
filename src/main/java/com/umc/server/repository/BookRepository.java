package com.umc.server.repository;

import com.umc.server.domain.Book;
import com.umc.server.domain.enums.MemberBookStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.id NOT IN :excludedBookIds")
    List<Book> findBooksNotInList(@Param("excludedBookIds") List<Long> excludedBookIds);

    @Query(
            "SELECT b FROM Book b "
                    + "JOIN b.memberBook mb "
                    + "WHERE mb.member.id = :memberId "
                    + "AND (:status IS NULL OR mb.memberBookStatus = :status) "
                    + "ORDER BY "
                    + "CASE WHEN :sortBy = 'rating_desc' THEN mb.score END DESC, "
                    + "CASE WHEN :sortBy = 'rating_asc' THEN mb.score END ASC, "
                    + "CASE WHEN :sortBy = 'newest' THEN b.createdAt END DESC, "
                    + "CASE WHEN :sortBy = 'oldest' THEN b.createdAt END ASC, "
                    + "CASE WHEN :sortBy = 'relevance' THEN b.title END ASC")
    Page<Book> findBooks(
            @Param("memberId") Long memberId,
            @Param("status") MemberBookStatus status,
            @Param("sortBy") String sortBy,
            Pageable pageable);

    Optional<Book> findByIsbn(String isbn);
}
