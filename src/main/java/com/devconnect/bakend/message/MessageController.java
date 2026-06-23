package com.devconnect.bakend.message;

import com.devconnect.bakend.message.dto.MessageRequest;
import com.devconnect.bakend.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/chat")
    public void sendMessage(MessageRequest request) {
        messageService.sendMessage(request);
    }

    @GetMapping("/api/messages/{username}")
    public ResponseEntity<Page<MessageResponse>> getChatHistory(
            @PathVariable String username,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(messageService.getChatHistory(username, pageable));
    }
}
