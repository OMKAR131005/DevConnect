package com.devconnect.notificationservice.notification;

import com.devconnect.notificationservice.config.RabbitMQConfig;
import com.devconnect.notificationservice.event.CommentEvent;
import com.devconnect.notificationservice.event.FollowEvent;
import com.devconnect.notificationservice.event.LikeEvent;
import com.devconnect.notificationservice.notification.dto.NotificationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(Message message) {
        String eventType = (String) message.getMessageProperties().getHeader("eventType");
        byte[] body = message.getBody();
        log.info("Received event of type: {}", eventType);

        try {
            switch (eventType) {
                case "LIKE" -> {
                    LikeEvent event = objectMapper.readValue(body, LikeEvent.class);
                    Notification notification = new Notification();
                    notification.setRecipientUserId(event.getPostOwnerId());
                    notification.setActorUserId(event.getActorUserId());
                    notification.setActorUsername(event.getActorUsername());
                    notification.setType(NotificationType.LIKE);
                    notification.setPostId(event.getPostId());
                    notificationRepository.save(notification);
                }
                case "COMMENT" -> {
                    CommentEvent event = objectMapper.readValue(body, CommentEvent.class);
                    Notification notification = new Notification();
                    notification.setRecipientUserId(event.getPostOwnerId());
                    notification.setActorUserId(event.getActorUserId());
                    notification.setActorUsername(event.getActorUsername());
                    notification.setType(NotificationType.COMMENT);
                    notification.setPostId(event.getPostId());
                    notification.setCommentText(event.getCommentText());
                    notificationRepository.save(notification);
                }
                case "FOLLOW" -> {
                    FollowEvent event = objectMapper.readValue(body, FollowEvent.class);
                    Notification notification = new Notification();
                    notification.setRecipientUserId(event.getTargetUserId());
                    notification.setActorUserId(event.getActorUserId());
                    notification.setActorUsername(event.getActorUsername());
                    notification.setType(NotificationType.FOLLOW);
                    notificationRepository.save(notification);
                }
                default -> log.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Failed to process event: {}", e.getMessage());
        }
    }
}