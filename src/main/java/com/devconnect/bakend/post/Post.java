package com.devconnect.bakend.post;

import com.devconnect.bakend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(length = 50,nullable = false)
    private String title;

    @Column(length =500 )
    private String description;

    private String imagePublicId;

    private String imageUrl;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private PostVisibility visibility;
    private String tags;
    private long viewCount;
    public void increaseViewCount(){
        this.viewCount++;
    }
}
