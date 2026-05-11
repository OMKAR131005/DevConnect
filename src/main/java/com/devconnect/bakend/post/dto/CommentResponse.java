package com.devconnect.bakend.post.dto;


import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentResponse {
    private Long commentId;
    private String username;
    private String profilePicture;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
