package com.umc.server.repository;

import com.umc.server.domain.DailyReading;
import com.umc.server.domain.mapping.MemberBook;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailyReadingRepository extends JpaRepository<DailyReading, Long> {

    @Query(
            "SELECT dr FROM DailyReading dr WHERE dr.memberBook = :memberBook AND FUNCTION('DATE', dr.createdAt) = :createdAt")
    Optional<DailyReading> findByMemberBookAndCreatedAt(
            @Param("memberBook") MemberBook memberBook, @Param("createdAt") LocalDate createdAt);

    @Query(
            "SELECT dr FROM DailyReading dr WHERE dr.memberBook = :memberBook ORDER BY dr.createdAt DESC")
    Optional<DailyReading> findLatestByMemberBook(@Param("memberBook") MemberBook memberBook);

    List<DailyReading> findAllByMemberBookIn(List<MemberBook> memberBookList);

    DailyReading findByMemberBook(MemberBook memberBook);
}
