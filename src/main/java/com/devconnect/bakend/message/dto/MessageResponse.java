package com.devconnect.bakend.message.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private String roomId;
    private LocalDateTime createdAt;
}