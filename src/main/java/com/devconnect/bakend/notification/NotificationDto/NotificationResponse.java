package com.devconnect.bakend.notification.NotificationDto;

import com.devconnect.bakend.notification.NotificationType;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationResponse {
    private Long notificationId;
    private String fromUsername;
    private String fromProfilePicture;
    private NotificationType notificationType;
    private String message;
    private boolean isRead;
    private Long postId;
    private LocalDateTime createdAt;
}