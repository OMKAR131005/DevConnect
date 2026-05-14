package com.devconnect.bakend.notification;

import com.devconnect.bakend.notification.NotificationDto.NotificationResponse;
import com.devconnect.bakend.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByToUserOrderByCreatedAtDesc(User toUser, Pageable pageable);
    @Modifying
    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime date);

    void deleteByFromUser(User user);

    void deleteByToUser(User user);
}
