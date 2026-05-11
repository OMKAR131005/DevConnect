package com.devconnect.bakend.post.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentRequest {
    private String commentText;
}
