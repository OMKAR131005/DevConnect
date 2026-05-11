package com.devconnect.bakend.post.dto;

import com.devconnect.bakend.post.PostVisibility;
import lombok.*;
import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PostResponse {
    private Long postId;
    private String username;
    private String profilePicture;
    private String title;
    private String description;
    private String tags;
    private String imageUrl;
    private PostVisibility visibility;
    private long likeCount;
    private long commentCount;
    private long viewCount;
    private boolean isLikedByMe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
