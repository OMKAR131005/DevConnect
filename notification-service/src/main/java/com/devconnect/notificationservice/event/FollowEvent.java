package com.devconnect.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowEvent {
    private Long actorUserId;
    private String actorUsername;
    private Long targetUserId;
}