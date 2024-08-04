package com.umc.server.repository;

import com.umc.server.domain.Club;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String name);

    Optional<Club> findByCode(String code);
}
