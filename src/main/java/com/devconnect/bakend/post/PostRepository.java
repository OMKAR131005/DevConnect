package com.devconnect.bakend.post;

import com.devconnect.bakend.post.dto.PostResponse;
import com.devconnect.bakend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    int countByUser(User user);
    @Query("SELECT new com.devconnect.bakend.post.dto.PostResponse(p.id, u.username, pr.profilePicture, p.title, p.description, p.tags, p.imageUrl, p.visibility, " +
            "(SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p), " +
            "(SELECT COUNT(c) FROM Comment c WHERE c.post = p), " +
            "p.viewCount, false, p.createdAt, p.updatedAt) " +
            "FROM Post p JOIN p.user u JOIN Profile pr ON pr.user = u " +
            "WHERE u = :user")
    Page<PostResponse> findPostsByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT new com.devconnect.bakend.post.dto.PostResponse(p.id, u.username, pr.profilePicture, p.title, p.description, p.tags, p.imageUrl, p.visibility, " +
            "(SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p), " +
            "(SELECT COUNT(c) FROM Comment c WHERE c.post = p), " +
            "p.viewCount, false, p.createdAt, p.updatedAt) " +
            "FROM Post p JOIN p.user u JOIN Profile pr ON pr.user = u " +
            "WHERE (u IN (SELECT f.following FROM Follow f WHERE f.follower = :me AND f.status = 'FOLLOWING') " +
            "AND p.visibility != 'ONLY_ME') OR u = :me")
    Page<PostResponse> getFeedPosts(@Param("me") User me, Pageable pageable);

    void deleteByUser(User user);
}
