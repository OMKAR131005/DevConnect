package com.devconnect.bakend.message.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {
    private String receiverUsername;
    private String messageText;
}