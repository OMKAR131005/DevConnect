package com.devconnect.bakend.post;

import com.devconnect.bakend.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    long countByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
    @Modifying
    @Transactional
    void deleteByUserAndPost(User user, Post post);

    @Modifying
    @Transactional
    void deleteByPost(Post post);

    void deleteByUser(User user);
}
