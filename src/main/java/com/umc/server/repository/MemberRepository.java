package com.umc.server.repository;

import com.umc.server.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);
}
