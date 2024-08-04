package com.umc.server.repository;

import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    Optional<MemberBook> findByIdAndMember(Long id, Member member);
}
