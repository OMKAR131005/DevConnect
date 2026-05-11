package com.devconnect.bakend.post.dto;
import com.devconnect.bakend.post.PostVisibility;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PostRequest {
    private String title;
    private String description;
    private String tags;
    private PostVisibility visibility;
}