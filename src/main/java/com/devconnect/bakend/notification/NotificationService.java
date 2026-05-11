package com.devconnect.bakend.notification;

import com.devconnect.bakend.exceptions.ResourceNotFoundException;
import com.devconnect.bakend.notification.NotificationDto.NotificationResponse;
import com.devconnect.bakend.post.Post;
import com.devconnect.bakend.profile.Profile;
import com.devconnect.bakend.profile.ProfileRepository;
import com.devconnect.bakend.user.User;
import com.devconnect.bakend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    public void createNotification(User toUser,User fromUser, NotificationType type, Post post, String message){
         Notification notification=Notification.builder().notificationType(type).toUser(toUser).message(message).fromUser(fromUser).post(post).build();
         notificationRepository.save(notification);
    }
    public Page<NotificationResponse> getNotifications(Pageable pageable) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Page<Notification> notifications = notificationRepository.findByToUserOrderByCreatedAtDesc(user, pageable);
        return notifications.map(notification -> {
            Profile profile = profileRepository.findByUser(notification.getFromUser());
            return NotificationResponse.builder()
                    .notificationId(notification.getNotificationId())
                    .fromUsername(notification.getFromUser().getUsername())
                    .fromProfilePicture(profile.getProfilePicture())
                    .notificationType(notification.getNotificationType())
                    .message(notification.getMessage())
                    .isRead(notification.isRead())
                    .postId(notification.getPost() != null ? notification.getPost().getId() : null)
                    .createdAt(notification.getCreatedAt())
                    .build();
        });
    }
    public boolean markAsRead(Long id){
        Notification notification=notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return true;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNotification(){
        notificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(15));
    }
}
