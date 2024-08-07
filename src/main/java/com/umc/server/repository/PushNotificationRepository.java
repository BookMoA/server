package com.umc.server.repository;

import com.umc.server.domain.PushNotification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    Optional<PushNotification> findByMemberId(Long memberId);
}
