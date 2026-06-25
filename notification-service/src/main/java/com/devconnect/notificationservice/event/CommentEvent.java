package com.devconnect.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {
    private Long actorUserId;
    private String actorUsername;
    private Long postId;
    private Long postOwnerId;
    private String commentText;
}
