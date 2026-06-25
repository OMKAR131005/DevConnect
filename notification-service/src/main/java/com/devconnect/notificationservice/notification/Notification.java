package com.devconnect.notificationservice.notification;



import com.devconnect.notificationservice.notification.dto.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientUserId;
    private Long actorUserId;
    private String actorUsername;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long postId;
    private String commentText;
    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
