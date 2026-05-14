package com.devconnect.bakend.post;

import com.devconnect.bakend.post.dto.CommentResponse;
import com.devconnect.bakend.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    long countByPost(Post post);
    @Query("SELECT new com.devconnect.bakend.post.dto.CommentResponse(c.commentId, u.username, pr.profilePicture, c.commentText, c.createdAt, c.updatedAt) " +
            "FROM Comment c JOIN c.user u JOIN Profile pr ON pr.user = u " +
            "WHERE c.post = :post")
    Page<CommentResponse> findCommentsByPost(@Param("post") Post post, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByPost(Post post);

    void deleteByUser(User user);
}
