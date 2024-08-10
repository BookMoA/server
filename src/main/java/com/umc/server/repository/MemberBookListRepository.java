package com.umc.server.repository;

import com.umc.server.domain.mapping.MemberBookList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBookListRepository extends JpaRepository<MemberBookList, Long> {
    Optional<MemberBookList> findByBookListIdAndMemberId(Long bookListId, Long memberId);
}
